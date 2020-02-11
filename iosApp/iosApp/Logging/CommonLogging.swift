//
//  CommonLogging.swift
//  iosApp
//
//  Created by Jamie Craane on 10/02/2020.
//

import Foundation
import common
import SwiftyBeaver

public struct CommonLogging {
    public static func initialize() {
        // We assign a delegate to the iosLoggingDelegate in common. The delegate uses SwiftyBeaver for logging.
        if (AppLoggerKt.iosLoggingDelegate == nil) {
            AppLoggerKt.iosLoggingDelegate = CommonLoggingDelegate()
        }
    }
}

class CommonLoggingDelegate : IOSLoggingDelegate {
    private let console = ConsoleDestination()
    var isDebugLoggingEnabled: Bool = true
    let log = SwiftyBeaver.self
    
    init() {
        console.format = "$DHH:mm:ss$d $N.$F:$l $L: $M"
        log.addDestination(console)
    }
    
    func error(message: String) {
        log.error(message)
    }
    
    func warn(message: String) {
        log.warning(message)
    }
    
    func debug(message: String) {
        log.debug(message)
    }
    
    func info(message: String) {
        log.info(message)
    }
}


