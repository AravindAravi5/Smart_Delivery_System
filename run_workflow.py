import pexpect
import sys
import re

def main():
    print("===========================================")
    print("  Executing Fully Automated Workflow...    ")
    print("===========================================\n")
    
    child = pexpect.spawn('mvn exec:java', encoding='utf-8', timeout=15)
    # Output directly to console (task logs)
    child.logfile_read = sys.stdout
    
    try:
        # Register Customer
        child.expect('Select an option:')
        child.sendline('2')
        child.expect('Enter username:')
        child.sendline('super_cust')
        child.expect('Enter password:')
        child.sendline('pass123')
        child.expect('Enter email:')
        child.sendline('super_cust@test.com')
        child.expect(r'Role \(ADMIN, CUSTOMER, AGENT, EMPLOYEE\):')
        child.sendline('CUSTOMER')
        
        # Register Agent
        child.expect('Select an option:')
        child.sendline('2')
        child.expect('Enter username:')
        child.sendline('super_agent')
        child.expect('Enter password:')
        child.sendline('pass123')
        child.expect('Enter email:')
        child.sendline('super_agent@test.com')
        child.expect(r'Role \(ADMIN, CUSTOMER, AGENT, EMPLOYEE\):')
        child.sendline('AGENT')
        
        # Register Admin
        child.expect('Select an option:')
        child.sendline('2')
        child.expect('Enter username:')
        child.sendline('super_admin')
        child.expect('Enter password:')
        child.sendline('pass123')
        child.expect('Enter email:')
        child.sendline('super_admin@test.com')
        child.expect(r'Role \(ADMIN, CUSTOMER, AGENT, EMPLOYEE\):')
        child.sendline('ADMIN')
        
        # Login Agent FIRST to set profile
        child.expect('Select an option:')
        child.sendline('1')
        child.expect('Enter username:')
        child.sendline('super_agent')
        child.expect('Enter password:')
        child.sendline('pass123')
        
        child.expect('Full Name:')
        child.sendline('Super Driver')
        child.expect('Phone:')
        child.sendline('8888888888') # VALID 10 digits
        child.expect(r'Vehicle Type \(e.g. Van, Bike\):')
        child.sendline('Van')
        child.expect('License Number:')
        child.sendline('LIC12345')
        child.expect('City:')
        child.sendline('Hyderabad')
        
        child.expect('Select:')
        child.sendline('3') # Logout
        
        # Login Customer
        child.expect('Select an option:')
        child.sendline('1')
        child.expect('Enter username:')
        child.sendline('super_cust')
        child.expect('Enter password:')
        child.sendline('pass123')
        
        # Complete Profile for Customer
        child.expect('Full Name:')
        child.sendline('Super Customer')
        child.expect('Phone:')
        child.sendline('1234567890')
        child.expect('Address:')
        child.sendline('123 Wonderland')
        child.expect('City:')
        child.sendline('Hyderabad')
        child.expect('Pincode:')
        child.sendline('500001')
        
        # Customer Menu -> Send Package
        child.expect('Select:')
        child.sendline('1')
        child.expect('Description:')
        child.sendline('Books')
        child.expect('Receiver City:')
        child.sendline('Bangalore')
        child.expect(r'Weight \(kg\):')
        child.sendline('1.5')
        
        # Customer Menu -> Logout
        child.expect('Select:')
        child.sendline('3') 
        
        # Login Admin
        child.expect('Select an option:')
        child.sendline('1')
        child.expect('Enter username:')
        child.sendline('super_admin')
        child.expect('Enter password:')
        child.sendline('pass123')
        
        # Admin Menu -> Process Dispatch
        child.expect('Select:')
        child.sendline('2')
        
        # Admin Menu -> Logout
        child.expect('Select:')
        child.sendline('3') 
        
        # Login Agent
        child.expect('Select an option:')
        child.sendline('1')
        child.expect('Enter username:')
        child.sendline('super_agent')
        child.expect('Enter password:')
        child.sendline('pass123')
        
        # Agent Menu -> View Assigned
        child.expect('Select:')
        child.sendline('1')
        
        # Agent Menu -> Mark Delivered
        child.expect('Select:')
        child.sendline('2')
        
        output_before = child.before
        match = re.search(r'ID: (\d+) \| Tracking:', output_before)
        if match:
            pkg_id = match.group(1)
            child.expect(r'Enter Package ID \(the number, e.g. 1\) to mark as DELIVERED:')
            child.sendline(pkg_id)
        else:
            child.expect(r'Enter Package ID \(the number, e.g. 1\) to mark as DELIVERED:')
            child.sendline('1') # fallback
        
        # Agent Logout
        child.expect('Select:')
        child.sendline('3')
        
        # Exit System
        child.expect('Select an option:')
        child.sendline('3')
        
        child.expect(pexpect.EOF, timeout=2)
    except pexpect.TIMEOUT:
        pass
    except Exception as e:
        print("\n\nError occurred: ", e)
    finally:
        print("\n\n--- Simulation Complete ---")

if __name__ == '__main__':
    main()
