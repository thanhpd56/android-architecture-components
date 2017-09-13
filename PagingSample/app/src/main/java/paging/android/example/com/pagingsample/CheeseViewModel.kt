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

package paging.android.example.com.pagingsample

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.paging.PagedList

/**
 * A simple ViewModel that provides a paged list of delicious Cheeses.
 */
class CheeseViewModel(app: Application) : AndroidViewModel(app) {
    val dao = CheeseDb.get(app).cheeseDao()
    companion object {
        /**
         * A good page size is a value that fills 2 screens worth of content so that the User is
         * unlikely to see a null item.
         * You can play with this constant to observe the paging behavior.
         */
        const val PAGE_SIZE = 30
        /**
         * If placeholders are enabled, PagedList will report the full size but some items might
         * be null in onBind method (PagedListAdapter triggers a rebind when data is loaded).
         *
         * If placeholders are disabled, onBind will never receive null but as more pages are
         * loaded, the scrollbars will jitter as new pages are loaded. You should probably disable
         * scrollbars if you disable placeholders.
         */
        const val ENABLE_PLACEHOLDERS = true
    }

    @Suppress("HasPlatformType")
    val allCheeses = dao.allCheesesByName().create(0, PagedList.Config.Builder().apply {
        setPageSize(PAGE_SIZE)
        setEnablePlaceholders(ENABLE_PLACEHOLDERS)
    }.build())

    fun insert(text: CharSequence) = ioThread {
        dao.insert(Cheese(id = 0, name = text.toString()))
    }

    fun remove(cheese: Cheese) = ioThread {
        dao.delete(cheese)
    }
}