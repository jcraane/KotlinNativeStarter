import UIKit
import common

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        
        let api = IosApiWrapper()
        api.retrievePersons(success: { [weak self] (persons: [Person]) in
            print("Success, got \(persons)")
            //self?.handle(persons: persons) Enable to demonstrate
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error; \(throwable?.description() ?? "")")
        })
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
    
    private func handle(persons: [Person]) {
        
        //Issue demonstrating https://github.com/JetBrains/kotlin-native/issues/2470 and https://github.com/JetBrains/kotlin-native/issues/2443
        
        let otherPersons = persons.map { (p) -> Person in
            return p
        }
        
        let otherArray: [Person] = Array(otherPersons)
        
        DispatchQueue.global(qos: .background).async {
            print("This is run on the background queue")
            
            otherArray.forEach { person in
                print("Person = \(person.firstName)")
            }
            
            DispatchQueue.main.async {
                print("This is run on the main queue, after the previous code in outer block")
                
                persons.forEach { person in
                    print("Person = \(person.firstName)")
                }
            }
        }
    }
}
