package com.gpm.myplans.ui.compose.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gpm.myplans.ui.theme.Purple40
import com.gpm.myplans.ui.theme.PurpleGrey40
import com.gpm.myplans.ui.theme.labelMedium
import com.gpm.myplans.ui.theme.labelMediumBold
import com.gpm.myplans.ui.theme.redDD2326

@Composable
fun GenericApiError(
    modifier: Modifier = Modifier,
    title: String? = null,
    @DrawableRes icon: Int? = null,
    message: String? = null,
    positiveActionTitle: String? = null,
    positiveAction: (() -> Unit)? = null,
    negativeActionTitle: String? = null,
    negativeAction: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        title?.let {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = title,
                style = labelMediumBold
            )
        }

        icon?.let {
            Image(
                modifier = Modifier.padding(12.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
            )
        }

        message?.let {
            Text(
                modifier = Modifier.padding(16.dp),
                text = message,
                style = labelMedium,
                textAlign = TextAlign.Center
            )
        }

        content?.invoke()

        positiveActionTitle?.let {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                SubmitButton(text = positiveActionTitle,
                    color = Color.White,
                    bgColor = redDD2326,
                    onClick = { positiveAction?.invoke() })
            }
        }

        negativeActionTitle?.let {
            Text(
                modifier = Modifier
                    .padding(top = if (positiveAction == null) 24.dp else 0.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .clickable { negativeAction?.invoke() },
                text = negativeActionTitle,
                color = Purple40
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SubmitButton(
    text: String, color: Color, bgColor: Color, onClick: () -> Unit,
    enabled: Boolean = true,
    disabledColor: Color = PurpleGrey40
) {
    Button(
        modifier = Modifier.wrapContentWidth(),
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            disabledContainerColor = disabledColor
        ),
        contentPadding = PaddingValues(14.dp),
        enabled = enabled
    ) {
        Text(
            modifier = Modifier
                .width(150.dp)
                .wrapContentHeight(),
            text = text,
            style = labelMediumBold.merge(color = color),
            textAlign = TextAlign.Center
        )
    }
}
