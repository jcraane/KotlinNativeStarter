import UIKit
import common

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var persons: [Person] = []
    let cellIdentifier = "CellIdentifier"
    @IBOutlet weak var myLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Persons"
    
        myLabel.text = "Hello"
        let api = ApiWrapper()
        api.retrievePersons(success: { [weak self] (persons: [Person]) in
            print("Success, got \(persons)")
            /*self?.persons.removeAll()
            self?.persons.append(contentsOf: persons)
            self?.tableView.reloadData()*/
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error; \(throwable?.description() ?? "")")
        })
        
        api.retrieveTasks(success: { [weak self] (tasks: [Task]) in
            print("Success, got \(tasks)")
            //self?.handle(persons: persons) Enable to demonstrate
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error; \(throwable?.description() ?? "")")
        })
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return persons.count
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath)
        let person = persons[indexPath.row]
        cell.textLabel?.text = person.fullname
        return cell
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
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
