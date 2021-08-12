***Project Documentation***
@Author Robert Meis

Introduction: 
---
***Ethereum and Smart Contracts***

Ethereum is a blockchain cryptocurrency technology that utilizes Smart Contracts. A Smart Contract is a custom-written program which can be deployed to the Ethereum blockchain with a 
defined amount of "funds." The Smart Contract also contains methods such as "transfer()" which define the rules under which those funds can be exchanged. Zuess uses an ERC-20 compliant
Smart Contract, which contain the standard set of functions (transfer(), allowance(), etc.) required for deployment on the public Ethereum Blockchain. 
For more information on Smart Contracts and the ERC-20 Standard, please visit this link: https://ethereum.org/en/developers/docs/standards/tokens/erc-20/


***Zuess Digital Currency Service Overview***
---
Our application, Zuess Digital Currency Service is a Spring MVC web application that aims to provide a simple and approachable user interface for the deployment and management of a private 
digital currency/cryptocurrency network using a Smart Contract and the Ethereum Blockchain. 

Application Pages: 

Admin Portal
---
In the Admin Portal Zuess allows a user with the click of a button to deploy a Smart Contract to an EXISTING Ethereum Blockchain (for example, a local Blockchain using the Ganache Blockchain test network (https://www.trufflesuite.com/ganache)).
Once the Smart Contract is deployed with a custom funds amount, an Admin can perform the following actions: 

1. Transfer Funds: transfer funds from the Admin account (containing the amount of funds set when the Smart Contract is deployed) to any other active account on the Ethereum blockchain
2. Display Balance: display the account balance of any valid Ethereum user
3. Grant Scholarship: as a demo for how a school might use a private digital currency network, it is possible to define an Ethereum Allowance from the Administrator ("School") account to 
a standard user account, allowing them to spend up to a certain amount from the school account. We have also created a demo store (see Store) section that allows you to define the items which 
maybe purchased with allowance funds (such as textbooks -- but not clothing).

Digital Wallet
---
The Digital Wallet functions similarly to commercial digital wallets on sites like MetaMask or RobinHood in that it allows a user to spend their Ethereum funds.

Store
---
The Store page currently simulates the CSUMB Bookstore. It allows you to add items to a cart and complete a purchase using (optionally) a combination of Scholarship and standard funds. Items
available in the Store have a "scholarship_eligible" attribute in their database representation which determines whether the item may be purchased with Scholarship funds.
A purchase transfers funds from the purchaser wallet to the store wallet. 

BlockChain Visualization
---
This page retrieves data from the Ethereum Blockchain and displays a visual of each block on the Blockchain. It also shows what type of transaction (funds transfer, etc.) is stored on the block,
although this feature is not fully finished and needs additional work. 

Help Page
---
The Help Page contains information on Blockchain Technology as well as videos demonstrating use of the application. 

Login/Registration
---
Creating an account in Zuess stores user information in a MySQL relational database, and also automatically assigns the user an unassigned Ethereum account ID. We use Spring Security and a 
role-based permissions system to delineate access between standard users and Administrators. 

***Instructions for Running the Application***
---
To run the application (in its current iteration), you must download the Ganache test blockchain network: https://www.trufflesuite.com/ganache
1. Start Ganache after installation
2. Choose "Quickstart Ethereum" option to create a one-click blockchain network.
3. Make sure the following settings are used in Ganache (suggest copy/paste from this document): Gas Price: 20000000000 Gas Limit: 500000000000 RPC Server: HTTP://127.0.0.1:7545
4. In an IDE such as Eclipse, AFTER starting Ganache with the above settings, right-click "Run" the file ZuessMvcApplication.java
5. Register a new account and login. Each time Ganache is restarted, the Ethereum account IDs change and so you ideally you should register a fresh user each time you restart the Ganache network.
6. On the Admin page, deploy a Smart Contract using the Ethereum Private Key of the FIRST account in Ganache. (This will define your user as the Contract owner).
7. From here, you can click through the app and do things such as transfer funds to other users or to yourself, view the actual state of the Blockchain, or purchase items in the store.

***Project Author Credits***
@Robert Meis: all Ethereum related code (i.e., Smart Contract code, BlockchainService) and all other backend code except for Spring Security. Database design, initial front-end code for all pages and subsequent styling. Server Side Input Validation in controller.
@Antonio Villagomez: substantial front-end styling on pages such as Admin, Wallet, Bookstore, Help Videos and Help Page.
@Jesse Krepelka: Spring Security implementation, Role-Based Permissions, Login, Registration pages, Input Validation for Login and Registration using Spring Security

***Major Technologies Used***
Solidity: Ethereum language for writing Smart Contracts
Ethereum Blockchain (Ganache): provides an immutable record of all transactions and fund balances for each user, among other things.
Spring: Java MVC framework
Spring Security: Robust and customizeable security framework
Web3J: Java library for interacting with an Ethereum Blockchain
MySQL: relational database used for storing user and application data
Jpa Repository: Java framework for database storage and retrieval

Contact project authors with questions:  
Robert Meis rmeis@csumb.edu 
Antonio Villagomez avillagomez@csumb.edu
Jesse Krepelka jkrepelka@csumb.edu














