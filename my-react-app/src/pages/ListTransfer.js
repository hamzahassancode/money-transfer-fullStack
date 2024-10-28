import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

function TransferList() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [accountsPerPage] = useState(5);

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = () => {
    axios.get('http://localhost:8080/v1/transfers')
      .then((response) => {
        setAccounts(response.data);
        setLoading(false);
      })
      .catch((error) => {
        setError('Error fetching accounts');
        setLoading(false);
      });
  };



  const deleteAllTransfers = () => {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete all transfers? This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete all!',
    }).then((result) => {
      if (result.isConfirmed) {
        axios.delete('http://localhost:8080/api/transfers')
          .then(() => {
            Swal.fire('Deleted!', 'All transfers have been deleted.', 'success');
            setAccounts([]); // Clear the list after deletion
          })
          .catch((error) => {
            console.error('Error deleting transfers', error);
            Swal.fire('Failed!', 'Failed to delete transfers.', 'error');
          });
      }
    });
  };

  const reversedAccounts = [...accounts].reverse();
  const indexOfLastAccount = currentPage * accountsPerPage;
  const indexOfFirstAccount = indexOfLastAccount - accountsPerPage;
  const currentAccounts = reversedAccounts.slice(indexOfFirstAccount, indexOfLastAccount);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  if (loading) {
    return <p className="text-center">Loading accounts...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">{error}</p>;
  }

  const totalPages = Math.ceil(accounts.length / accountsPerPage);

  return (
    <div className="relative flex items-center justify-center min-h-screen p-4"
      style={{
        backgroundImage: "url('/list-transfer.webp')",
        backgroundSize: '2150px',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        width: '100%',
        height: '100%',
      }}
    >
      {/* Dark overlay */}
      <div className="absolute inset-0 bg-black opacity-20 z-0"></div>

      <div className="relative z-10 bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold mb-6 text-center text-gray-700">Transfers List</h1>

        {/* Conditionally show a message if no transfers exist */}
        {accounts.length === 0 && (
          <p className="text-center text-gray-600 mb-4">No transfers available.</p>
        )}

        {/* Transfer list */}
        <div className="overflow-y-auto max-h-96">
          <ul className="space-y-4">
            {currentAccounts.map((account) => (
              <li key={account.id} className="border-b last:border-b-0">
                <div className="flex justify-between items-center p-4  transition duration-150 ease-in-out">
                  <div>
                    <h2 className="text-lg font-semibold text-gray-800">Beneficiary Account:</h2>
                    <p className="text-md text-gray-600">{account.beneficiaryAccount}</p>
                    <p className="text-md text-gray-600">Status: {account.status}</p>

                    <p className="text-sm text-gray-500">Date: {new Date(account.date).toLocaleString()}</p>
                  </div>
                  <span className="text-xl font-bold text-green-600">{account.currency} {account.amount.toFixed(2)}</span>
                </div>
              </li>
            ))}
          </ul>
        </div>

        {/* Pagination controls */}
        <div className="flex justify-center mt-4">
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index + 1}
              onClick={() => paginate(index + 1)}
              className={`mx-1 px-3 py-1 rounded-lg ${currentPage === index + 1 ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'}`}
            >
              {index + 1}
            </button>
          ))}
        </div>

        {/* Conditionally render the delete all button */}
        {accounts.length > 0 && (
          <div className="flex justify-center mt-6">
            <button
              onClick={deleteAllTransfers}
              className="bg-gray-200 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition duration-150 ease-in-out"
            >
              Delete All Transfers
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default TransferList;
