import UIKit
import common

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        
        let api = IosApiWrapper()
        api.retrievePersons(success: { ([Person]) in
            print("Success")
        }, failure: {(throwable: KotlinThrowable?) in
            print("Error")
        })
        
//        let persons = api.retrievePersons()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
}
