import UIKit
import common

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        
        let api = IosApiWrapper()
//        api.retrievePersons(success: { ([Person]) -> Person in
//            print("dsda")
//        }, failure: <#T##(KotlinThrowable?) -> KotlinUnit#>)
        
//        let persons = api.retrievePersons()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
}
