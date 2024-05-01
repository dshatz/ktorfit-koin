package com.dshatz.ktorfitkoin

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration

class Processor(val codeGenerator: CodeGenerator) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotations = listOf("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        val ktorFitFiles = annotations.flatMap { resolver.getSymbolsWithAnnotation("de.jensklingenberg.ktorfit.http.$it") }
            .distinctBy {
                it.location
            }.map { it.parent as KSDeclaration }.groupBy { it.packageName.asString() }
        ktorFitFiles.forEach { (pckg, declarations) ->
            val files = declarations.map { it.containingFile }.filterNotNull().toTypedArray()
            val output = codeGenerator.createNewFile(Dependencies(true, sources = files), pckg, "KtorfitModule")
            output.bufferedWriter().use {
                fun writeLn(t: String) = it.write(t + "\n")

                writeLn("package $pckg")
                it.write("""
                    import org.koin.core.module.Module
                    import org.koin.dsl.*
                    import de.jensklingenberg.ktorfit.Ktorfit
                    
                """.trimIndent())
                writeLn("public val ktorFitModule: Module = module {")
                declarations.forEach {
                    writeLn("   single<${it.qualifiedName?.asString()}>() { val ktorFit: Ktorfit = get(); ktorFit.create() } ")
                }
                writeLn("}")
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