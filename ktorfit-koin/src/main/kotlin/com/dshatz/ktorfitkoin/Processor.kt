package com.dshatz.ktorfitkoin

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import java.io.OutputStream

class Processor(val codeGenerator: CodeGenerator, val options: Map<String, String>) : SymbolProcessor {

    lateinit var log: OutputStream
    fun emit(s: String, indent: String = "") {
        with(log) {
            this.write((s + "\n").encodeToByteArray())
        }
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
//        log = codeGenerator.createNewFile(Dependencies(false), "", "KtorfitKoin", "log")
//        emit("Processing")
        val ktorFitFiles = resolver.getSymbolsWithAnnotation("de.jensklingenberg.ktorfit.http.GET")
            .distinctBy {
                it.location
            }.map { it.parent as KSDeclaration }.groupBy { it.packageName.asString() }
        ktorFitFiles.forEach { (pckg, declarations) ->
            val files = declarations.map { it.containingFile }.filterNotNull().toTypedArray()
//            emit(file.toString())
//            emit(pckg)
//            emit(declarations.toString())
//            emit("\n")
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
        return Processor(environment.codeGenerator, environment.options)
    }

}