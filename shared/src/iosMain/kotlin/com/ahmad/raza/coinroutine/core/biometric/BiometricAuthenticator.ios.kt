package com.ahmad.raza.coinroutine.core.biometric

import androidx.compose.runtime.Composable
import com.ahmad.raza.coinroutine.biometric.IosBiometricAuthenticator

object IosPlatformContext : PlatformContext

@Composable
actual fun getPlatformContext(): PlatformContext = IosPlatformContext


actual fun getBiometricAuthenticator(context: PlatformContext): BiometricAuthenticator =
    IosBiometricAuthenticator()