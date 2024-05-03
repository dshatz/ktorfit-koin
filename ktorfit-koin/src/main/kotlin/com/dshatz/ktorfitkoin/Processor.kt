package com.dshatz.ktorfitkoin

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName

class Processor(val codeGenerator: CodeGenerator) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotations = listOf("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        val modules = resolver.getSymbolsWithAnnotation("com.dshatz.ktorfitkoin.annotation.ServiceModule").filterIsInstance<KSClassDeclaration>()
            .map { it to it.annotations.first { it.shortName.asString() == "ServiceModule" }.arguments.first { it.name?.asString() == "scanPackage" }.value as String }.toList()
        val ktorFitFiles = annotations.flatMap { resolver.getSymbolsWithAnnotation("de.jensklingenberg.ktorfit.http.$it") }
            .distinctBy {
                it.location
            }.map { it.parent as KSDeclaration }.groupBy { it.packageName.asString() }
        ktorFitFiles.forEach { pckg, declarations ->
            // pckg - unique package name
            // declarations - list of Service declarations in this package.
//            error(modules.toList())
            val module = modules.first { (mod, arg) -> arg == pckg }
            val files = declarations.map { it.containingFile }.filterNotNull().toTypedArray()
            val moduleName = module.first.simpleName.asString()
            val moduleNameLower = moduleName.replaceFirstChar { it.lowercaseChar() }
            val output = codeGenerator.createNewFile(Dependencies(true, sources = files), module.second, module.first.simpleName.asString())
            output.bufferedWriter().use {
                fun writeLn(t: String) = it.write(t + "\n")
                it.write("""
                    package $pckg
                    
                    import org.koin.core.module.Module
                    import org.koin.dsl.*
                    import de.jensklingenberg.ktorfit.Ktorfit
                    import ${module.first.qualifiedName?.asString()}
                    
                    val ${moduleNameLower}: Module = module {
                    
                """.trimIndent())
                declarations.distinctBy { it.qualifiedName?.asString() }.forEach {
                    writeLn("   single<${it.qualifiedName?.asString()}>() { val ktorFit: Ktorfit = get(); ktorFit.create() }")
                }
                writeLn("}")
                val outputExtension = codeGenerator.createNewFile(Dependencies(true, sources = files), "org.koin.ksp.generated", moduleNameLower)
                    .bufferedWriter()
                outputExtension.use {
                    it.write("""
                    package org.koin.ksp.generated
                    import org.koin.core.module.Module
                    import ${pckg}.*
                    import ${module.first.qualifiedName?.asString()}
                    
                    val ${moduleName}.module: Module get() = $moduleNameLower
                """.trimIndent())
                }
            }

        }

        return emptyList()
    }

}

class Provider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.codeGenerator)
    }

}