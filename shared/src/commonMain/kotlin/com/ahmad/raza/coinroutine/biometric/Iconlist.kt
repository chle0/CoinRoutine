package com.ahmad.raza.coinroutine.biometric

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import coinroutine.shared.generated.resources.Res
import coinroutine.shared.generated.resources.ic_face_id
import coinroutine.shared.generated.resources.ic_fingerprint
import com.ahmad.raza.coinroutine.Platform
import com.ahmad.raza.coinroutine.platform
import org.jetbrains.compose.resources.vectorResource

val BiometricIcon: ImageVector
    @Composable
    get() = when (platform) {
        is Platform.Android -> vectorResource(Res.drawable.ic_fingerprint)
        is Platform.Ios -> vectorResource(Res.drawable.ic_face_id)
    }