package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import io.seon.androidsdk.exception.SeonException
import io.seon.androidsdk.service.SeonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }

        repeat(1) { index ->
            GlobalScope.launch(Dispatchers.IO) {
                val sessionID = UUID.randomUUID().toString()
                val sfp = SeonBuilder().withContext(applicationContext).withSessionId(sessionID).build()
                sfp.setLoggingEnabled(true)
                val start = System.currentTimeMillis()
                println("io.seon.log[$index]--Start:$start")
                try {
                    sfp.getFingerprintBase64 { seonFingerprint: String? ->
                        //set seonFingerprint as the value for the session
                        //property of your Fraud API request.
                        val end = System.currentTimeMillis()
                        println("io.seon.log[$index]--End:$end. Took ~ ${(end-start)/1000}s")
                        println("io.seon.log[$index]--TOKEN:$seonFingerprint}")
                    }
                } catch (e : SeonException){
                    e.printStackTrace()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}