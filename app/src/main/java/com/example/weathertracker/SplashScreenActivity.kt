package com.example.weathertracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathertracker.ui.theme.WeatherTrackerTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherTrackerTheme {
                SplashScreen {
                    // Navigate to MainActivity after animation
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    // Scale animation for main icon
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Alpha animation for text
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 500,
            easing = LinearEasing
        ),
        label = "alpha"
    )

    // Floating animation for weather icons
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Start animation and navigate after delay
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // 3 seconds splash screen
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A90E2),
                        Color(0xFF357ABD),
                        Color(0xFF2E5A8B)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated weather icons in background
        WeatherIconsBackground(float)

        // Main content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main weather icon with scale animation
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App name with fade animation
            Text(
                text = "Weather Tracker",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline with fade animation
            Text(
                text = "Your Weather Companion",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Loading indicator
            if (startAnimation) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .alpha(alpha),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            }
        }

        // Version or powered by text at bottom
        Text(
            text = "Version 1.0",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(alpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    WeatherTrackerTheme {
        SplashScreen(onTimeout = {})
    }
}

@Composable
fun WeatherIconsBackground(offset: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Floating sun icon - top right
        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .offset(y = offset.dp)
                .align(Alignment.TopEnd)
                .padding(top = 100.dp, end = 40.dp)
                .alpha(0.2f),
            tint = Color.White
        )

        // Floating rain drop - top left
        Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .offset(y = (-offset).dp)
                .align(Alignment.TopStart)
                .padding(top = 150.dp, start = 30.dp)
                .alpha(0.2f),
            tint = Color.White
        )

        // Floating cloud - bottom left
        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .offset(y = offset.dp)
                .align(Alignment.BottomStart)
                .padding(bottom = 120.dp, start = 20.dp)
                .alpha(0.15f),
            tint = Color.White
        )

        // Floating wind icon - bottom right
        Icon(
            imageVector = Icons.Default.Air,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .offset(y = (-offset).dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 160.dp, end = 50.dp)
                .alpha(0.2f),
            tint = Color.White
        )

        // Small cloud - center left
        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .offset(y = (-offset * 1.5f).dp)
                .align(Alignment.CenterStart)
                .padding(start = 60.dp)
                .alpha(0.1f),
            tint = Color.White
        )

        // Small sun - center right
        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .offset(y = (offset * 1.3f).dp)
                .align(Alignment.CenterEnd)
                .padding(end = 70.dp)
                .alpha(0.15f),
            tint = Color.White
        )
    }
}
