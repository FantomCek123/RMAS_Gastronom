package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.models.entities.Comment

@Composable
fun CommentsSection(comments: List<Comment>) {
    Column(modifier = Modifier.padding(16.dp)) {
        comments.forEach { comment ->
            CommentItem(comment)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}