package net.tandem.component.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.tandem.data.model.Comparable

open class BaseViewHolder<B : ViewDataBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root)

abstract class BasePagedAdapter<T : Comparable, B : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>? = null, private val layoutId: Int
) : PagingDataAdapter<T, BaseViewHolder<B>>(diffCallback ?: DefaultDiffCallback<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context), layoutId, parent, false
        )
        return BaseViewHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<B>, position: Int) {
        getItem(position)?.let { item ->
            onBindView(holder.binding, item, position, null)
        }
    }

    @CallSuper
    override fun onBindViewHolder(
        holder: BaseViewHolder<B>, position: Int, payloads: MutableList<Any>
    ) {
        getItem(position)?.let { item ->
            onBindView(holder.binding, item, position, payloads)
        }
    }

    abstract fun onBindView(binding: B, item: T, position: Int, payloads: MutableList<Any>?)
}