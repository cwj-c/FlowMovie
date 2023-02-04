
interface ImplementationItem {
    val implementations: List<String>
}

interface AndroidTestImplementationItem {
    val androidTestImplementations: List<String>
}

interface TestImplementationItem {
    val testImplementations: List<String>
}

interface KaptItem {
    val kapts: List<String>
}

interface AnnotationProcessorItem {
    val annotationProcessors: List<String>
}

object Dependencies {
    object Version {
        //AndroidX
        const val appCompat = "1.5.1"
        const val material = "1.7.0"
        const val constraintLayout = "2.1.4"

        //Ktx
        const val ktx = "1.9.0"
        const val activityKtx = "1.7.0-alpha02"
        const val fragmentKtx = "1.6.0-alpha03"

        //Retrofit
        const val retrofit = "2.9.0"

        //Okhttp
        const val okhttp = "4.10.0"

        //Glide
        const val glide = "4.14.2"

        //Dagger-Hilt
        const val hilt = "2.44"

        //Room
        const val room = "2.5.0"

        //Timber
        const val timber = "5.0.1"

        //Coroutine Test
        const val coroutineTest = "1.6.4"

        //Test
        const val junit = "1.1.3"
        const val assertj = "3.20.2"
        const val mockk = "1.13.2"

        //Android Test
        const val espresso = "3.4.0"
    }

    object Libraries {

        object AndroidX : ImplementationItem {
            private const val appCompat = "androidx.appcompat:appcompat:${Dependencies.Version.appCompat}"
            private const val material = "com.google.android.material:material:${Dependencies.Version.material}"
            private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Dependencies.Version.constraintLayout}"

            override val implementations = listOf(
                appCompat, material, constraintLayout
            )
        }

        object Ktx : ImplementationItem {
            private const val ktxCore = "androidx.core:core-ktx:${Version.ktx}"
            private const val activityKtx = "androidx.activity:activity-ktx:${Dependencies.Version.activityKtx}"
            private const val fragmentKtx = "androidx.fragment:fragment-ktx:${Dependencies.Version.fragmentKtx}"

            override val implementations = listOf(
                ktxCore, activityKtx, fragmentKtx
            )
        }

        object Retrofit : ImplementationItem {
            private const val retrofit = "com.squareup.retrofit2:retrofit:${Dependencies.Version.retrofit}"
            private const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"

            override val implementations = listOf(
                retrofit, gsonConverter
            )
        }

        object Okhttp : ImplementationItem {
            private const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"

            override val implementations = listOf(
                loggingInterceptor
            )
        }

        object Glide : ImplementationItem, KaptItem {
            private const val glide = "com.github.bumptech.glide:glide:${Dependencies.Version.glide}"
            private const val glideCompiler = "com.github.bumptech.glide:compiler:${Version.glide}"

            override val implementations = listOf(glide)
            override val kapts = listOf(glideCompiler)
        }

        object Hilt : ImplementationItem, KaptItem {
            private const val hilt = "com.google.dagger:hilt-android:${Dependencies.Version.hilt}"
            private const val hiltCompiler = "com.google.dagger:hilt-compiler:${Version.hilt}"

            override val implementations = listOf(hilt)
            override val kapts = listOf(hiltCompiler)
        }

        object Room : ImplementationItem, KaptItem {
            private const val room = "androidx.room:room-runtime:${Dependencies.Version.room}"
            private const val roomComplier = "androidx.room:room-compiler:${Dependencies.Version.room}"
            private const val roomKtx = "androidx.room:room-ktx:${Dependencies.Version.room}"

            override val implementations = listOf(room, roomKtx)
            override val kapts = listOf(roomComplier)
        }

        object Timber : ImplementationItem {
            private const val timber = "com.jakewharton.timber:timber:${Dependencies.Version.timber}"

            override val implementations = listOf(timber)
        }

        object CoroutineTest : TestImplementationItem, AndroidTestImplementationItem {
            private const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.Version.coroutineTest}"

            override val testImplementations = listOf(coroutineTest)

            override val androidTestImplementations = listOf(coroutineTest)
        }

        object Test : TestImplementationItem {
            private const val junit = "junit:junit:"
            private const val assertj = "org.assertj:assertj-core:${Dependencies.Version.assertj}"
            private const val mockk = "io.mockk:mockk:${Dependencies.Version.mockk}"

            override val testImplementations = listOf(
                junit, assertj, mockk
            )
        }

        object AndroidTest : AndroidTestImplementationItem {
            private const val junit = "androidx.test.ext:junit:${Dependencies.Version.junit}"
            private const val espressoCore = "androidx.test.espresso:espresso-core:${Version.espresso}"
            private const val assertj = "org.assertj:assertj-core:${Dependencies.Version.assertj}"
            private const val mockk = "io.mockk:mockk-android:${Dependencies.Version.mockk}"

            override val androidTestImplementations = listOf(
                junit, espressoCore, assertj, mockk
            )
        }

    }
}