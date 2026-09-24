plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.android")
 id("org.jetbrains.kotlin.plugin.compose")
}
android {
 namespace = "com.dzmarketplace.app"
 compileSdk = 35
 defaultConfig {
  applicationId = "com.dzmarketplace.app"
  minSdk = 24
  targetSdk = 35
  versionCode = 1
  versionName = "1.0"
 }
 buildTypes {
  release {
   // Sign the release variant with the standard Android debug key so the APK is installable.
   // For Play Store/production distribution, replace this with a private release keystore.
   signingConfig = signingConfigs.getByName("debug")
  }
 }
}
kotlin { jvmToolchain(17) }
dependencies {
 implementation(platform("androidx.compose:compose-bom:2024.12.01"))
 implementation("androidx.activity:activity-compose:1.10.0")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
 implementation("androidx.navigation:navigation-compose:2.8.5")
}