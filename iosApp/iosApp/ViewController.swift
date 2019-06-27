import UIKit
import common

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        
        let api = IosApiWrapper()
        api.retrievePersons(success: { (persons: [Person]) in
            print("Success, got \(persons)")
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error")
        })
        
//        let persons = api.retrievePersons()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
}
