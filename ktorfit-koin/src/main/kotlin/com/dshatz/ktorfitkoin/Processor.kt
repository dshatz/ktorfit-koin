package com.dshatz.ktorfitkoin

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.Module
import java.util.*

class Processor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotations = listOf("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")

        /**
         * Map of @ServiceModule declarations -> scanPackage value.
         */
        val modules = resolver.getSymbolsWithAnnotation("com.dshatz.ktorfitkoin.annotation.ServiceModule").filterIsInstance<KSClassDeclaration>()
            .map { it to it.annotations.first { it.shortName.asString() == "ServiceModule" }.arguments.first { it.name?.asString() == "scanPackage" }.value as String }.toList()
        val ktorFitFiles = annotations.flatMap { resolver.getSymbolsWithAnnotation("de.jensklingenberg.ktorfit.http.$it") }
            .distinctBy {
                it.location
            }.map { it.parent as KSDeclaration }.groupBy { it.packageName.asString() }

        for ((pckg, declarations) in ktorFitFiles) {
            // pckg - unique package name
            // declarations - list of Service declarations in this package.
//            error(modules.toList())
            val module = modules.firstOrNull { (mod, arg) -> arg == pckg } ?: run {
                logger.warn(
                    """
                               [ktorfit-koin] The following modules were not scanned by any @ServiceModule and thus will not be included in any Koin module. 
                               ${declarations.joinToString("\n") { it.qualifiedName!!.asString() }}
                               
                               To create a module with those Ktorfit services, define a new Module for each (or parent) package. Example below.
                                   @ServiceModule(packageScan = "${declarations.first().packageName.asString()}")
                                   class KtorfitModule
                                   
                               You can still provide those manually using standard koin.
                                   
                            """.trimIndent()
                )
                null
            } ?: continue
            logger.info("[ktorfit-koin] @ServiceModule ${module.first.qualifiedName?.asString()} scan detected ${declarations.size} Ktorfit services")
            val files = declarations.map { it.containingFile }.filterNotNull().toTypedArray()
            val moduleName = module.first.simpleName.asString()
            val moduleType = module.first.asStarProjectedType().toTypeName()

            val moduleNameLower = moduleName.replaceFirstChar { it.lowercaseChar() }

            val file = FileSpec.builder(pckg, moduleName)
            file
                .addImport("org.koin.dsl", "module")
                .addImport(Module::class, "")
                .addImport(Ktorfit::class, "")
                .addProperty(
                    PropertySpec.builder(moduleNameLower, Module::class.asClassName())
                        .initializer(CodeBlock.builder()
                            .beginControlFlow("module")
                            .apply {
                                declarations.distinctBy { it.qualifiedName?.asString() }.forEach {
                                    val cl = ClassName(it.packageName.asString(), it.simpleName.asString())
                                    addStatement("single<%T> { val ktorFit: %T = get(); ktorFit.create() }", cl, Ktorfit::class)
                                }
                            }
                            .endControlFlow()
                            .build()
                        ).build()
                )
                .build()
                .writeTo(codeGenerator, Dependencies(false, sources = files))

            val markerName = listOf("koin", "def") + module.first.qualifiedName!!.asString().split(".")
            FileSpec.builder("org.koin.ksp.generated", moduleNameLower)
                .addImport(Module::class, "")
                .addImport(pckg, moduleNameLower)
                .addProperty(
                    PropertySpec.builder("module", Module::class)
                        .receiver(moduleType)
                        .getter(
                            FunSpec.getterBuilder()
                                .addCode("return $moduleNameLower").build()
                        )
                        .build()
                )
                .addType(TypeSpec.classBuilder(markerName.joinToString("") { it.myCapitalize() }).build())
                .build()
                .writeTo(codeGenerator, Dependencies(false, sources = files))
        }

        return emptyList()
    }

}

class Provider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.codeGenerator, environment.logger)
    }

}

fun String.myCapitalize() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }