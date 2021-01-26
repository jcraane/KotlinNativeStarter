import UIKit

import common

import RxSwift
import RxCocoa

import SwiftyBeaver
let log = SwiftyBeaver.self

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var persons: [Person] = []
    let cellIdentifier = "CellIdentifier"
    @IBOutlet weak var myLabel: UILabel!
    
    @IBOutlet weak var personsTable: UITableView!
    
    @IBOutlet weak var retrievePersonsButton: UIButton!

    private let api = IosApiWrapper()

    private let disposeBag = DisposeBag()

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Persons"    
        myLabel.text = NSLocalizedString("button.ok", comment: "")
        personsTable.delegate = self
        personsTable.dataSource = self
        personsTable.register(UITableViewCell.self, forCellReuseIdentifier: "CellIdentifier")
        retrievePersonsButton.addTarget(self, action: #selector(didButtonClick), for: .touchUpInside)

        Driver
            .just("Test")
            .flatMapLatest { _ in
                return asObservable(self.api.complexFlow())
                        .asDriver(onErrorJustReturn: "test")
            }
            .debug("driver")
            .drive(onNext: { next in
                print("next value is \(next)")
            })
            .disposed(by: disposeBag)

        print("Hallo Elin!")

    }
    
    @objc func didButtonClick(_ sender: UIButton) {
//        retrievePersons()
        
        //testFlow()

    }
    
    func testFlow() {
        api.testFlow(success: {(result: String) in
            print("Value from flow is \(result)")
        })
    }
    
    func retrievePersons() {
        persons = []
        personsTable.reloadData()
                
        api.retrievePersons(
            success: { [weak self] (persons: [Person]) in
                print("Person 1 age = \(persons[0].age)")
//                test mutation
                persons[0].age = 100
                print("Person 1 (after write) age = \(persons[0].age)")
                print("Person 2 (after write) age = \(persons[1].age)")
                self?.persons = persons
                self?.personsTable.reloadData()
                print("Success, got \(persons)")
                
                self?.handle(persons: persons)
            },
            failure: { (failure: Failure) in
                print("Failure in calling retrievePersons: \(failure.status)")
                if (failure.status == Int32(-1009) || failure.status == Int32(-1004)) {
                    print("Internet Offline")
                } else {
                    print("Error: \(failure.message ?? "")")
                }
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
            return NativeStateHelperKt.freeze(p) as! Person
//            return p
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
                
//                attempt of mutating state of frozen object
//                persons[0].age = 1000
            }
        }
    }
}
