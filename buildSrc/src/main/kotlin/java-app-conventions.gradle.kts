plugins {
    id("java-common-conventions")
    id("org.springframework.boot")
    id("org.graalvm.buildtools.native")
}

graalvmNative {
    testSupport.set(false)
}
