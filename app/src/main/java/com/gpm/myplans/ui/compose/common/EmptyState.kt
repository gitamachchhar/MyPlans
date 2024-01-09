package com.gpm.myplans.ui.compose.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gpm.myplans.R
import com.gpm.myplans.ui.theme.Purple40
import com.gpm.myplans.ui.theme.labelMediumBold

@Preview
@Composable
fun EmptyState(msg: String = "", stateImage: Int = 0) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Image(
            modifier = Modifier
                .height(250.dp)
                .width(220.dp),
            painter = painterResource(id = stateImage),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = msg,
            style = labelMediumBold,
            color = Purple40
        )
    }
}
