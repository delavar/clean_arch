package com.delavar.arch.domain.usecase

import com.delavar.arch.domain.executor.DomainScheduler
import com.delavar.arch.domain.response.ErrorResponse
import com.delavar.arch.domain.response.SuccessResponse
import com.delavar.arch.domain.response.UseCaseResponse
import com.delavar.arch.domain.util.DomainErrorUtils
import com.delavar.arch.domain.response.ErrorStatus
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase<T>(
    private val scheduler: DomainScheduler
) {
    /**
     * should be override by child UseCase and will return desired [Flowable] created by repositories
     */
    protected abstract fun buildUseCaseObservable(): Flowable<T>

    /**
     * subscribed to [Flowable] that returns from [buildUseCaseObservable] and send response via lambda callback.
     *
     * @param onResponse a lambda function that receives a [UseCaseResponse] in order to invoke when result is available or an error occurred
     * @param onTokenExpire a nullable lambda function to invoke when token expired and repository returned UNAUTHORIZED
     *
     * @return returns created disposable
     */
    fun execute(
        compositeDisposable: CompositeDisposable,
        onResponse: (UseCaseResponse<T>) -> Unit,
        onTokenExpire: (() -> Unit)? = null
    ): Disposable {
        return this.buildUseCaseObservable()
            .onBackpressureLatest()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe({ onResponse(SuccessResponse(it)) },
                {
                    val error = DomainErrorUtils.getErrorModel(it)

                    if (error.errorStatus == ErrorStatus.UNAUTHORIZED)
                        onTokenExpire?.invoke()

                    onResponse(ErrorResponse(error))
                }).also { compositeDisposable.add(it) }
    }
}