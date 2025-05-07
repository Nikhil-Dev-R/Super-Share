package com.rudraksha.supershare.core.domain.repository

import com.rudraksha.supershare.core.domain.model.AppItem
import com.rudraksha.supershare.core.domain.model.AudioItem
import com.rudraksha.supershare.core.domain.model.ContactItem
import com.rudraksha.supershare.core.domain.model.FileItem
import com.rudraksha.supershare.core.domain.model.MediaItem
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem

interface PickerRepository {
    suspend fun getInstalledApps(): List<AppItem>
    suspend fun getFiles(): List<FileItem>
    suspend fun getVideos(): List<MediaItem>
    suspend fun getPhotos(): List<MediaItem>
    suspend fun getSongs(): List<AudioItem>
    suspend fun getContacts(): List<ContactItem>
    suspend fun getHistory(): List<HistoryItem>
}
