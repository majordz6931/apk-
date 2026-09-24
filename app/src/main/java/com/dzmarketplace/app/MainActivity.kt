package com.dzmarketplace.app
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

enum class Page { DASHBOARD, SETTINGS, LOGS }

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent { App(applicationContext) }
 }
}

@Composable fun App(context: Context) {
 val prefs = remember { context.getSharedPreferences("dzmarketplace", 0) }
 var loggedIn by remember { mutableStateOf(prefs.getBoolean("login", false)) }
 var page by remember { mutableStateOf(Page.DASHBOARD) }
 MaterialTheme {
  if (!loggedIn) {
   Login { prefs.edit().putBoolean("login", true).apply(); loggedIn = true }
  } else {
   Main(page, { page = it }) { prefs.edit().putBoolean("login", false).apply(); loggedIn = false }
  }
 }
}

@Composable fun Login(onLogin: () -> Unit) {
 var key by remember { mutableStateOf("") }
 var show by remember { mutableStateOf(false) }
 Surface(Modifier.fillMaxSize()) {
  Column(Modifier.fillMaxSize().padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) {
   Text("DZ Marketplace", style = MaterialTheme.typography.headlineLarge)
   Spacer(Modifier.height(8.dp))
   Text("منصة إدارة السوق")
   Spacer(Modifier.height(28.dp))
   OutlinedTextField(
    value = key, onValueChange = { key = it }, modifier = Modifier.fillMaxWidth(),
    label = { Text("License / Access Key") }, singleLine = true,
    visualTransformation = if (show) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
    trailingIcon = { TextButton(onClick = { show = !show }) { Text(if (show) "إخفاء" else "إظهار") } }
   )
   Spacer(Modifier.height(14.dp))
   Button(onClick = onLogin, modifier = Modifier.fillMaxWidth(), enabled = key.isNotBlank()) { Text("دخول") }
  }
 }
}

@Composable fun Main(page: Page, nav: (Page) -> Unit, logout: () -> Unit) {
 Scaffold(
  topBar = { TopAppBar(title = { Text("DZ Marketplace") }) },
  bottomBar = {
   NavigationBar {
    NavigationBarItem(selected = page == Page.DASHBOARD, onClick = { nav(Page.DASHBOARD) }, icon = {}, label = { Text("الرئيسية") })
    NavigationBarItem(selected = page == Page.SETTINGS, onClick = { nav(Page.SETTINGS) }, icon = {}, label = { Text("الإعدادات") })
    NavigationBarItem(selected = page == Page.LOGS, onClick = { nav(Page.LOGS) }, icon = {}, label = { Text("السجل") })
   }
  }
 ) { padding ->
  Box(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
   when (page) {
    Page.DASHBOARD -> Dashboard()
    Page.SETTINGS -> Settings(logout)
    Page.LOGS -> Logs()
   }
  }
 }
}

@Composable fun Dashboard() {
 var name by remember { mutableStateOf("") }
 var running by remember { mutableStateOf(false) }
 Column(Modifier.fillMaxSize()) {
  Text("لوحة التحكم", style = MaterialTheme.typography.headlineMedium)
  Spacer(Modifier.height(16.dp))
  OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("اسم القناة / الخدمة") }, singleLine = true)
  Spacer(Modifier.height(16.dp))
  Card(Modifier.fillMaxWidth()) {
   Column(Modifier.padding(18.dp)) {
    Text("الحالة: " + if (running) "تعمل" else "متوقفة", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Text("الاتصالات النشطة: —")
    Text("الأخطاء: —")
    Spacer(Modifier.height(14.dp))
    Button(onClick = { running = !running }, modifier = Modifier.fillMaxWidth(), enabled = name.isNotBlank()) {
     Text(if (running) "إيقاف" else "بدء")
    }
   }
  }
 }
}

@Composable fun Settings(logout: () -> Unit) {
 var auto by remember { mutableStateOf(false) }
 Column(Modifier.fillMaxSize()) {
  Text("الإعدادات", style = MaterialTheme.typography.headlineMedium)
  Spacer(Modifier.height(16.dp))
  Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
   Text("بدء تلقائي", Modifier.weight(1f))
   Switch(checked = auto, onCheckedChange = { auto = it })
  }
  HorizontalDivider()
  Spacer(Modifier.height(18.dp))
  Text("الاتصال الخارجي", style = MaterialTheme.typography.titleMedium)
  Text("يمكن ربط الواجهة بخدمة أو API رسمية ومصرح بها.", style = MaterialTheme.typography.bodySmall)
  Spacer(Modifier.height(24.dp))
  OutlinedButton(onClick = logout, modifier = Modifier.fillMaxWidth()) { Text("تسجيل الخروج") }
 }
}

@Composable fun Logs() {
 val logs = listOf("Application started", "DZ Marketplace ready", "Settings loaded", "Waiting for an authorized API connection")
 Column(Modifier.fillMaxSize()) {
  Text("سجل النظام", style = MaterialTheme.typography.headlineMedium)
  Spacer(Modifier.height(12.dp))
  LazyColumn { items(logs) { Text("• " + it, Modifier.padding(vertical = 7.dp)) } }
 }
}