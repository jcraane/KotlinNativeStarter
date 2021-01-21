//
// Created by Robert van Loghem on 21/01/2021.
//

import Foundation

import RxSwift
import RxCocoa

import common

public func asObservable<T>(_ flow: CFlow<T>) -> Observable<T?> {
    Observable.create { observer in
        let closable = flow.watch(block: { object in
            observer.onNext(object)
        })
        return Disposables.create {
            closable.close()
        }
    }
}
