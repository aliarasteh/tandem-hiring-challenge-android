package net.tandem.data

import androidx.paging.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by Ali Arasteh
 */

// region Paging Related Functions

open class PagerBuilder<T : Any, A> {
    internal var pageSize: Int = 10
    internal var databaseQuery: (() -> PagingSource<Int, T>)? = null
    internal var networkCall: (suspend (page: Int) -> Response<A>)? = null
    internal var saveNetworkResultStrategy: (suspend (A?, LoadType, Int) -> Unit)? = null
    internal var mapNetworkResponseStrategy: ((A?) -> List<T>)? = null
    internal var reachedEndStrategy: ((A?, page: Int) -> Boolean)? = null

    fun withPageSize(pageSize: Int) = apply {
        this.pageSize = pageSize
    }

    fun withDatabaseQuery(databaseQuery: () -> PagingSource<Int, T>) = apply {
        this.databaseQuery = databaseQuery
    }

    fun withNetworkCall(networkCall: suspend (page: Int) -> Response<A>) = apply {
        this.networkCall = networkCall
    }

    fun withSaveNetworkResultStrategy(strategy: suspend (A?, LoadType, Int) -> Unit) = apply {
        this.saveNetworkResultStrategy = strategy
    }

    fun withMapNetworkResponseStrategy(strategy: (A?) -> List<T>) = apply {
        this.mapNetworkResponseStrategy = strategy
    }

    fun withReachedEndStrategy(strategy: (A?, page: Int) -> Boolean) = apply {
        this.reachedEndStrategy = strategy
    }

    fun build(): Pager<Int, T> {
        return if (databaseQuery != null && networkCall != null) {
            if (saveNetworkResultStrategy == null)
                throw java.lang.Exception("No strategy considered for saving data fetched from network!")
            else if (reachedEndStrategy == null)
                throw java.lang.Exception("No strategy considered for detecting when reached end page!")

            createPagerWithDatabaseAndNetwork(
                pageSize = pageSize,
                databaseQuery = databaseQuery!!,
                networkCall = networkCall!!,
                saveCallResult = saveNetworkResultStrategy!!,
                reachedEndStrategy = reachedEndStrategy!!

            )
        } else if (networkCall != null) {
            if (mapNetworkResponseStrategy == null)
                throw java.lang.Exception("No strategy considered for mapping data fetched from network!")
            else if (reachedEndStrategy == null)
                throw java.lang.Exception("No strategy considered for detecting when reached end page!")

            createPagerWithNetworkOnly(
                pageSize = pageSize,
                networkCall = networkCall!!,
                reachedEndStrategy = reachedEndStrategy!!,
                mapResponse = mapNetworkResponseStrategy!!
            )
        } else if (databaseQuery != null) {
            createPagerWithDatabaseOnly(
                pageSize = pageSize,
                databaseQuery = databaseQuery!!
            )
        } else {
            throw java.lang.Exception("PagerBuilder not configured correctly!")
        }
    }
}

/**
 * creates Pager for using in paging architecture. creates RemoteMediator
 * the database serves as the single source of truth.
 * therefore UI can receive data updates from database only.
 * fetched data from network will be saved in database.
 * @param pageSize: number of items loaded per page
 * @param databaseQuery: query for loading data from database
 * @param networkCall: network call for loading data from network
 * @param saveCallResult: this function is meant to save network result into database
 * @param reachedEndStrategy: this function checks when last page reached
 *  */
@OptIn(ExperimentalPagingApi::class)
fun <T : Any, A> createPagerWithDatabaseAndNetwork(
    pageSize: Int,
    databaseQuery: () -> PagingSource<Int, T>,
    networkCall: suspend (page: Int) -> Response<A>,
    saveCallResult: suspend (A?, LoadType, Int) -> Unit,
    reachedEndStrategy: (A?, page: Int) -> Boolean
): Pager<Int, T> = Pager(
    config = PagingConfig(pageSize),
    remoteMediator = object : RemoteMediator<Int, T>() {
        private var pageCount = -1
        private var reachedEnd = false

        override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
            return try {
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageCount = 1
                    }
                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    LoadType.APPEND -> {
                        if (reachedEnd) return MediatorResult.Success(endOfPaginationReached = true)
                    }
                }

                // try loading data from network
                val result = getResult { networkCall.invoke(pageCount) }
                if (result.status == Result.Status.SUCCESS) {
                    // save data loaded from network into database
                    saveCallResult(result.data, loadType, pageCount)

                    pageCount++

                    // check if reached last page
                    reachedEnd = reachedEndStrategy.invoke(result.data, pageCount)
                    MediatorResult.Success(endOfPaginationReached = reachedEnd)
                } else {
                    MediatorResult.Error(result.error ?: Throwable(result.message))
                }
            } catch (e: IOException) {
                MediatorResult.Error(e)
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }

    }
) {
    databaseQuery.invoke()
}

/**
 * creates Pager for using in paging architecture. creates RemoteMediator
 * the database serves as the single source of truth.
 * therefore UI can receive data updates from database only.
 * @param pageSize: number of items loaded per page
 * @param databaseQuery: query for loading data from database
 *  */
fun <T : Any> createPagerWithDatabaseOnly(
    pageSize: Int,
    databaseQuery: () -> PagingSource<Int, T>
): Pager<Int, T> = Pager(
    config = PagingConfig(pageSize)
) {
    databaseQuery.invoke()
}

/**
 * Creates Pager for using in paging architecture, creates PagingSource
 * The network serves as the single source of truth.
 * Therefore UI can receive data updates from network only.
 * @param pageSize: number of items loaded per page
 * @param networkCall: network call for loading data from network
 * @param mapResponse: maps response to desire object list
 *  */
fun <T : Any, A> createPagerWithNetworkOnly(
    pageSize: Int = 10,
    networkCall: suspend (page: Int) -> Response<A>,
    mapResponse: (A?) -> List<T>,
    reachedEndStrategy: (A?, page: Int) -> Boolean
): Pager<Int, T> = Pager(
    config = PagingConfig(pageSize)
) {
    object : PagingSource<Int, T>() {
        private val STARTING_PAGE_INDEX = 1

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
            return try {
                val pageNumber = params.key ?: STARTING_PAGE_INDEX

                // try loading data from network
                val result = getResult { networkCall(pageNumber) }
                if (result.status == Result.Status.SUCCESS) {
                    val reachedEnd = reachedEndStrategy.invoke(result.data, pageNumber)
                    LoadResult.Page(
                        itemsAfter = 0,
                        data = mapResponse.invoke(result.data),
                        prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1,
                        nextKey = if (reachedEnd) null else pageNumber + 1
                    )
                } else {
                    LoadResult.Error(result.error ?: Throwable(result.message))
                }
            } catch (e: IOException) {
                LoadResult.Error(e)
            } catch (e: HttpException) {
                LoadResult.Error(e)
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
        override fun getRefreshKey(state: PagingState<Int, T>): Int? {
            // We need to get the previous key (or next key if previous is null) of the page
            // that was closest to the most recently accessed index.
            // Anchor position is the most recently accessed index
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }

    }
}
// endregion

// region Helper Functions
suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null)
                return Result.success(body)
            else if (isVoidType<T>())
                return Result.success()
        }
        val error = HttpException(response)
        return Result.error(error = error)
    } catch (e: Exception) {
        val message = e.message ?: e.toString()
        return Result.error(message, e)
    }
}

class Generic<T>

fun <T> isVoidType(): Boolean {
    return Generic<T>()::class.java == Generic<Void>()::class.java
}

/**
 * in case LoadState is Error this function gets related error from loadState
 * */
fun CombinedLoadStates.getError(): Throwable? {
    val errorState = this.source.append as? LoadState.Error
        ?: this.source.prepend as? LoadState.Error
        ?: this.append as? LoadState.Error
        ?: this.prepend as? LoadState.Error
        ?: this.mediator?.refresh as? LoadState.Error

    return errorState?.error
}

// endregion