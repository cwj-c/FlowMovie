import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kapt(item: KaptItem) {
    item.kapts.forEach {
        add("kapt", it)
    }
}

fun DependencyHandler.implementation(item: ImplementationItem) {
    item.implementations.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.androidTestImplementation(item: AndroidTestImplementationItem) {
    item.androidTestImplementations.forEach {
        add("androidTestImplementation", it)
    }
}

fun DependencyHandler.testImplementation(item: TestImplementationItem) {
    item.testImplementations.forEach {
        add("testImplementation", it)
    }
}

fun DependencyHandler.annotationProcessor(item: AnnotationProcessorItem) {
    item.annotationProcessors.forEach {
        add("annotationProcessor", it)
    }
}

