import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.asciidoctor.gradle.jvm.pdf.AsciidoctorPdfTask

plugins {
    `java-library`
    id("org.asciidoctor.jvm.convert") version "3.1.0"
    id("org.asciidoctor.jvm.pdf") version "3.1.0"
}

allprojects {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/central/")
        mavenLocal()
        mavenCentral()
    }
}

tasks.named<AsciidoctorTask>("asciidoctor") {
    baseDirIsRootProjectDir()
    baseDirIsProjectDir()
    baseDirFollowsSourceDir()
    baseDirFollowsSourceFile()

    sourceDir(file("OpenGL"))
    sources(delegateClosureOf<PatternSet> {
        include("OpenGL.adoc")
    })
    setOutputDir(file("build/html"))
}

tasks.named<AsciidoctorPdfTask>("asciidoctorPdf") {
    baseDirIsRootProjectDir()
    baseDirIsProjectDir()
    baseDirFollowsSourceDir()
    baseDirFollowsSourceFile()
    sourceDir(file("OpenGL"))
    sources(delegateClosureOf<PatternSet> {
        include("OpenGL.adoc")
    })
    setOutputDir(file("build/pdf"))
    setFontsDir("docs/font")
    setTheme("KaiGenGothicCN")
}

pdfThemes {
    local("KaiGenGothicCN") {
        themeDir = file("docs/themes")
        themeName = "KaiGenGothicCN"
    }
    local("basic") {
        themeDir = file("docs/themes")
        themeName = "very-basic"
    }
}