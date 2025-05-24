package com.ucb.ucbtest.plan

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ucb.domain.Plan
import com.ucb.ucbtest.R
import com.ucb.ucbtest.navigation.Screen

private fun openWhatsApp(context: Context, phone: String, message: String) {
    val uri = Uri.parse("whatsapp://send?phone=$phone&text=${Uri.encode(message)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val webUri = Uri.parse("https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(message)}")
        context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}

@Composable
fun PlanScreen(
    navController: NavHostController,
    viewModel: PlanViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val plan = state.plans.getOrNull(state.currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        // Header section
        Text(
            text = "Nuestros planes móviles",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "La mejor cobertura, increíbles beneficios\ny un compromiso con el planeta.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                lineHeight = 20.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Plan card section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f),
            contentAlignment = Alignment.TopCenter
        ) {
            plan?.let {
                PlanCard(
                    plan = it,
                    isPrevEnabled = state.currentIndex > 0,
                    isNextEnabled = state.currentIndex < state.plans.lastIndex,
                    onPrevClick = viewModel::previousPlan,
                    onNextClick = viewModel::nextPlan,
                    navController = navController
                )
            } ?: Text(
                text = "Cargando…",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PlanCard(
    plan: Plan,
    isPrevEnabled: Boolean,
    isNextEnabled: Boolean,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Plan title
            Text(
                text = plan.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B),
                    fontSize = 24.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    append("Antes ")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    ) {
                        append(" ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W900,
                            textDecoration = TextDecoration.LineThrough
                        )
                    ) {
                        append(plan.priceBefore)
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    ) {
                        append("/ mes ")
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    ) {
                        append("Ahora ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.W900,
                            color = Color.Black
                        )
                    ) {
                        append("${plan.priceNow}")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    ) {
                        append(" /mes")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            Text(
                text = plan.bandwidth,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Gray,
                    fontSize = 28.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation buttons

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .clickable(enabled = isPrevEnabled) { onPrevClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPrevEnabled) R.drawable.ic_arrow_left else R.drawable.ic_arrow_left_disabled
                        ),
                        contentDescription = "Anterior",
                        modifier = Modifier.size(20.dp),
                        tint = if (isPrevEnabled) Color(0xFFFFFFFF) else Color(0xFFCECECE)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .clickable(enabled = isNextEnabled) { onNextClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isNextEnabled) R.drawable.ic_arrow_right else R.drawable.ic_arrow_right_disabled
                        ),
                        contentDescription = "Siguiente",
                        modifier = Modifier.size(20.dp),
                        tint = if (isNextEnabled) Color(0xFFFFFFFF) else Color(0xFFCECECE)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Features list
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4CAF50) // Green checkmark
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Social media icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                listOf(
                    R.drawable.ic_whatsapp,
                    R.drawable.ic_facebook,
                    R.drawable.ic_messenger,
                    R.drawable.ic_x,
                    R.drawable.ic_instagram,
                    R.drawable.ic_snapchat,
                    R.drawable.ic_telegram
                ).forEach { iconRes ->
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { navController.navigate(Screen.MapScreen.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = "Quiero este plan",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
                val context = LocalContext.current
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-16).dp, y = (-24).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF25D366))
                        .clickable {
                            openWhatsApp(context,"+59179963639", "Hola, quiero este plan ${plan.name}")

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}