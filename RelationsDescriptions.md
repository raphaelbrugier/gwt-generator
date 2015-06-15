

# Introduction #

Please note that :

A _one_ cardinality is represented by _"1"_ or nothing

A _many_ cardinality is represented by _"`*`"_

For a bidirectional relation, you must add a stereotype < < owner > > on the owner's side.


---


---



# One To One relations descriptions #

## Unidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOne.png](http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOne.png)

### Generated Code ###
#### Body class : ####
```
@Entity
public class Body implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToOne
    Heart heart;

    public Body() {
    }
    
    public Body(boolean dummy) {
        this.heart = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Body createBody() {
        Body body = new Body();
        return body;
    }
    
    public Heart getHeart() {
        return heart;
    }
    
    public void setHeart(Heart heart) {
        this.heart = heart;
    }
}
```

#### Heart class : ####
```
@Entity
public class Heart implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;

    public Heart() {
    }
    
    public Heart(boolean dummy) {
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Heart createHeart() {
        Heart heart = new Heart();
        return heart;
    }
}
```


---


## Unidirectional with composition ##
![http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOneComposition.png](http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOneComposition.png)

### Generated code ###
#### LeftEntity class : ####
```
@Entity
public class LeftEntity implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToOne(cascade={CascadeType.REMOVE})
    RightEntity link;

    public LeftEntity() {
    }
    
    public LeftEntity() {
        this.link = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static LeftEntity createLeftEntity() {
        LeftEntity leftEntity = new LeftEntity();
        return leftEntity;
    }
    
    public RightEntity getLink() {
        return link;
    }
    
    public void setLink(RightEntity link) {
        this.link = link;
    }
}
```
#### RightEntity class : ####
```

@Entity
public class RightEntity implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;

    public RightEntity() {
    }
    
    public RightEntity(boolean dummy) {
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static RightEntity createRightEntity() {
        RightEntity rightEntity = new RightEntity();
        return rightEntity;
    }
}
```


---


## Bidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOneBi.png](http://gwt-generator.googlecode.com/svn/wiki/relations/oneToOneBi.png)

### Generated code ###

#### Customer class : ####
```
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToOne
    Passport passport;
    @Transient
    boolean inDeletion = false;

    public Customer() {
    }
    
    public Customer(boolean dummy) {
        this.passport = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Customer createCustomer() {
        Customer customer = new Customer();
        return customer;
    }
    
    public Passport getPassport() {
        return passport;
    }
    
    public void setPassport(Passport passport) {
        if (this.passport == passport)
            return;
        if (passport != null && passport.customer != null)
            passport.customer.passport = null;
        if (this.passport != null)
            this.passport.customer = null;
        this.passport = passport;
        if (passport != null)
            passport.customer = this;
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            if (this.passport != null && !this.passport.inDeletion)
                this.passport.customer = null;
        }
    }
}
```

#### Passport class : ####
```
@Entity
public class Passport implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToOne(mappedBy="passport")
    Customer customer;
    @Transient
    boolean inDeletion = false;

    public Passport() {
    }
    
    public Passport(boolean dummy) {
        this.customer = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Passport createPassport() {
        Passport passport = new Passport();
        return passport;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        if (this.customer == customer)
            return;
        if (customer != null && customer.passport != null)
            customer.passport.customer = null;
        if (this.customer != null)
            this.customer.passport = null;
        this.customer = customer;
        if (customer != null)
            customer.passport = this;
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            if (this.customer != null && !this.customer.inDeletion)
                this.customer.passport = null;
        }
    }
}
```



---


---

# One To Many relations descriptions #
## Unidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/oneToMany.png](http://gwt-generator.googlecode.com/svn/wiki/relations/oneToMany.png)

### Generated code ###

#### Trainer class : ####
```
@Entity
public class Trainer implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToMany
    List<Tiger> trainedTigers;

    public Trainer() {
    }
    
    public Trainer(boolean dummy) {
        this.trainedTigers = new ArrayList<Tiger>();
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Trainer createTrainer() {
        Trainer trainer = new Trainer();
        trainer.trainedTigers = new ArrayList<Tiger>();
        return trainer;
    }
    
    public List<Tiger> getTrainedTigers() {
        return Collections.unmodifiableList(trainedTigers);
    }
    
    public boolean addTrainedTiger(Tiger trainedTiger) {
        if (trainedTiger==null)
            throw new NullPointerException();
        if (!this.trainedTigers.contains(trainedTiger)) {
            this.trainedTigers.add(trainedTiger);
            return true;
        }
        else
            return false;
    }
    
    public void loadTrainedTigers() {
        if (!Hibernate.isInitialized(trainedTigers))
            Hibernate.initialize(trainedTigers);
    }
}
```

#### Tiger class : ####
```
@Entity
public class Tiger implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;

    public Tiger() {
    }
    
    public Tiger(boolean dummy) {
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Tiger createTiger() {
        Tiger tiger = new Tiger();
        return tiger;
    }
}
```



---

## Bidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/oneToManyBi.png](http://gwt-generator.googlecode.com/svn/wiki/relations/oneToManyBi.png)

Bidirectional relation with the oneToMany side as the owner is not supported yet.


---


---

# Many To One relations descriptions #

## Unidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToOne.png](http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToOne.png)

### Generated code ###
#### Flight class : ####
```
@Entity
public class Flight implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @ManyToOne
    Company company;

    public Flight() {
    }
    
    public Flight(boolean dummy) {
        this.company = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Flight createFlight() {
        Flight flight = new Flight();
        return flight;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
}
```

#### Company class : ####
```
@Entity
public class Company implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;

    public Company() {
    }
    
    public Company(boolean dummy) {
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Company createCompany() {
        Company company = new Company();
        return company;
    }
}
```


---

## Bidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToOneBi.png](http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToOneBi.png)

### Generated code ###

#### Soldier class : ####
```
@Entity
public class Soldier implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @ManyToOne
    Troop troop;
    @Transient
    boolean inDeletion = false;

    public Soldiers() {
    }
    
    public Soldier(boolean dummy) {
        this.troop = null;
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Soldier createSoldier() {
        Soldier soldier = new Soldier();
        return soldier;
    }
    
    public Troop getTroop() {
        return troop;
    }
    
    public void setTroop(Troop troop) {
        if (this.troop == troop)
            return;
        if (this.troop != null)
            this.troop.soldiers.remove(this);
        this.troop = troop;
        if (troop != null)
            troop.soldiers.add(this);
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            if (this.troop != null && !this.troop.inDeletion)
                this.troop.soldiers.remove(this);
        }
    }
}
```

#### Troop class : ####
```
@Entity
public class Troop implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @OneToMany(mappedBy="troop")
    List<Soldier> soldiers;
    @Transient
    boolean inDeletion = false;

    public Troop() {
    }
    
    public Troop(boolean dummy) {
        this.soldiers = new ArrayList<Soldier>();
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Troop createTroop() {
        Troop troop = new Troop();
        troop.soldiers = new ArrayList<Soldier>();
        return troop;
    }
    
    public List<Soldier> getSoldiers() {
        return Collections.unmodifiableList(soldiers);
    }
    
    public boolean addSoldier(Soldier soldier) {
        if (soldier==null)
            throw new NullPointerException();
        if (soldier.troop != this) {
            this.soldiers.add(soldier);
            if (soldier.troop != null)
                soldier.troop.soldiers.remove(this);
            soldier.troop = this;
            return true;
        }
        else
            return false;
    }
    
    public void loadSoldiers() {
        if (!Hibernate.isInitialized(soldiers))
            Hibernate.initialize(soldiers);
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            for (Soldier soldier : soldiers) {
                if (!soldier.inDeletion)
                    soldier.troop = null;
            }
        }
    }
    
    public boolean removeSoldier(Soldier soldier) {
        if (soldier==null)
            throw new NullPointerException();
        if (soldier.troop == this) {
            this.soldiers.remove(soldier);
            soldier.troop = null;
            return true;
        }
        else
            return false;
    }
}
```


---


---

# Many To Many relations descriptions #

## Unidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToMany.png](http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToMany.png)

### Generated code ###

#### Store class : ####
```
@Entity
public class Store implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @ManyToMany
    List<City> localizations;

    public Store() {
    }
    
    public Store(boolean dummy) {
        this.localizations = new ArrayList<City>();
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Store createStore() {
        Store store = new Store();
        store.localizations = new ArrayList<City>();
        return store;
    }
    
    public List<City> getLocalizations() {
        return Collections.unmodifiableList(localizations);
    }
    
    public boolean addLocalization(City localization) {
        if (localization==null)
            throw new NullPointerException();
        if (!this.localizations.contains(localization)) {
            this.localizations.add(localization);
            return true;
        }
        else
            return false;
    }
    
    public void loadLocalizations() {
        if (!Hibernate.isInitialized(localizations))
            Hibernate.initialize(localizations);
    }
    
    public boolean removeLocalization(City localization) {
        if (localization==null)
            throw new NullPointerException();
        if (this.localizations.contains(localization)) {
            this.localizations.remove(localization);
            return true;
        }
        else
            return false;
    }
}
```

#### City class : ####
```
@Entity
public class City implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;

    public City() {
    }
    
    public City(boolean dummy) {
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static City createCity() {
        City city = new City();
        return city;
    }
}
```


---


## Bidirectional ##

![http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToManyBi.png](http://gwt-generator.googlecode.com/svn/wiki/relations/ManyToManyBi.png)

### Generated code ###

#### Store class : ####
```
@Entity
public class Store implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @ManyToMany
    List<Customer> customers;
    @Transient
    boolean inDeletion = false;

    public Store() {
    }
    
    public Store(boolean dummy) {
        this.customers = new ArrayList<Customer>();
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Store createStore() {
        Store store = new Store();
        store.customers = new ArrayList<Customer>();
        return store;
    }
    
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }
    
    public boolean addCustomer(Customer customer) {
        if (customer==null)
            throw new NullPointerException();
        if (!this.customers.contains(customer)) {
            this.customers.add(customer);
            customer.stores.add(this);
            return true;
        }
        else
            return false;
    }
    
    public void loadCustomers() {
        if (!Hibernate.isInitialized(customers))
            Hibernate.initialize(customers);
    }
    
    public boolean removeCustomer(Customer customer) {
        if (customer==null)
            throw new NullPointerException();
        if (this.customers.contains(customer)) {
            this.customers.remove(customer);
            customer.stores.remove(this);
            return true;
        }
        else
            return false;
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            for (Customer customer : customers) {
                if (!customer.inDeletion)
                    customer.stores.remove(this);
            }
        }
    }
}
```

#### Customer class : ####
```
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue
    int id;
    @Version
    int version;
    @ManyToMany(mappedBy="customers")
    List<Store> stores;
    @Transient
    boolean inDeletion = false;

    public Customer() {
    }
    
    public Customer(boolean dummy) {
        this.stores = new ArrayList<Store>();
    }

    public int getId() {
        return id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public static Customer createCustomer() {
        Customer customer = new Customer();
        customer.stores = new ArrayList<Store>();
        return customer;
    }
    
    public List<Store> getStores() {
        return Collections.unmodifiableList(stores);
    }
    
    public boolean addStore(Store store) {
        if (store==null)
            throw new NullPointerException();
        if (!this.stores.contains(store)) {
            this.stores.add(store);
            store.customers.add(this);
            return true;
        }
        else
            return false;
    }
    
    public void loadStores() {
        if (!Hibernate.isInitialized(stores))
            Hibernate.initialize(stores);
    }
    
    public boolean removeStore(Store store) {
        if (store==null)
            throw new NullPointerException();
        if (this.stores.contains(store)) {
            this.stores.remove(store);
            store.customers.remove(this);
            return true;
        }
        else
            return false;
    }
    
    @PreRemove
    void preRemove() {
        if (!inDeletion) {
            inDeletion = true;
            for (Store store : stores) {
                if (!store.inDeletion)
                    store.customers.remove(this);
            }
        }
    }
}
```