package net.tandem.component.paging

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.tandem.data.model.Comparable

/**
 * Created by Ali Arasteh
 */

/**
 *  [DefaultDiffCallback] is default diffCallback used in [PagingDataAdapter]
 *  for comparing objects
 * */
class DefaultDiffCallback<T : Comparable> : DiffUtil.ItemCallback<T>() {
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.contentEqualsTo(newItem)

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.objectEqualsTo(newItem)
}