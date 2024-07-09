# spring-banking-api
## WORK IN PROGRESS

### Functional Requirements
- User Management
    - Registration: Users can register with personal information and initial deposit.
    - Login/Logout: Users can log in and log out securely.
    - Profile Management: Users can update their profile information.
    - Role Management: Admins can assign roles to users (e.g., customer, bank staff, admin).
    - Admin roles: See user list, view user details, active and deactivate users.

- Account Management
    - Create Account: Users can create various types of bank accounts (e.g., savings, checking).
    - View Account: Users can view their account details, balance and transaction history.
    - Update Account: Users can update account settings.
    - Delete Account: Admins can close accounts.
    
- Transaction Management
    - Deposit: Users can deposit money into their accounts.
    - Transfer: Users can transfer money between accounts.
    - Interest: Interest is calculated and paid automatically by system (interest rate depends upon the type of account).

---

### Notes
- Send email on successful deposits.
- Send email to sender and receiver on a successful fund transfer.
- Any list response should be paginated.

