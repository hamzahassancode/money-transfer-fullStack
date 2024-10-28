import React, { useEffect, useState } from 'react';
import axios from 'axios';


function AccountsList() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/v1/accounts')
      .then((response) => {
        setAccounts(response.data);
        setLoading(false);
      })
      .catch((error) => {
        setError('Error fetching accounts');
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p className="text-center">Loading accounts...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">{error}</p>;
  }

  return (
    <div className="relative flex items-center justify-center min-h-screen bg-gray-100 p-4"
      style={{
        backgroundImage: "url('/bank-account.png')", 
        backgroundSize: 'cover',     // Ensures the image covers the container
        backgroundRepeat: 'no-repeat', // Prevents the image from repeating
        backgroundPosition: 'center', // Centers the image
        width: '100%',               // Width of the container
        height: '100%' 
      }}>

      {/* Dark Overlay */}
      <div className="absolute inset-0 bg-black opacity-20"></div>

      {/* Content */}
      <div className="relative bg-white p-8 rounded-lg shadow-lg w-full max-w-md z-10">
        <h1 className="text-3xl font-bold mb-6 text-center text-gray-700">Accounts List</h1>
        <ul className="space-y-4">
          {accounts.map((account) => (
            <li key={account.accountNumber} className="border-b last:border-b-0">
              <div className="flex justify-between items-center p-4  transition duration-150 ease-in-out">
                <div>
                  <h2 className="text-lg font-semibold text-gray-800">Account Number:</h2>
                  <p className="text-md text-gray-600">{account.accountNumber}</p>
                </div>
                <span className="text-xl font-bold text-green-600">${account.amount.toFixed(2)}</span>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default AccountsList;
