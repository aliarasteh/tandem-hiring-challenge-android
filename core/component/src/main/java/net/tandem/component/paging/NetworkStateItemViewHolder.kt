/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tandem.component.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import net.tandem.component.R

/**
 * Created by Ali Arasteh
 */

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(
    parent: ViewGroup, private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
    private val loadingView: View by lazy { itemView.findViewById(R.id.loadingView) }
    private val loadFailView: View by lazy { itemView.findViewById(R.id.loadFailView) }
    private val retryButton: View by lazy { itemView.findViewById(R.id.retryButton) }

    fun bindTo(loadState: LoadState) {
        loadingView.isVisible = loadState is Loading
        loadFailView.isVisible = loadState is Error
        retryButton.also {
            it.setOnClickListener { retryCallback() }
        }
    }

}
