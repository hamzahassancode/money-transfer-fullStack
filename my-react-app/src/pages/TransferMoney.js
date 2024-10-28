import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';

function TransferMoney() {
    const [accounts, setAccounts] = useState([]);
    const [debitAccount, setDebitAccount] = useState('');
    const [amount, setAmount] = useState('');
    const [beneficiaryAccount, setBeneficiaryAccount] = useState('');
    const [beneficiaryType, setBeneficiaryType] = useState('');
    const [date, setDate] = useState(''); // New state for date

    const navigate = useNavigate();

    useEffect(() => {
        axios.get('http://localhost:8080/v1/accounts')
            .then((response) => {
                setAccounts(response.data);
            })
            .catch((error) => {
                console.error('Error fetching accounts:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Failed to load accounts. Please try again later.',
                    confirmButtonColor: '#d33',
                    confirmButtonText: 'OK',
                });
            });
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        const transferDetails = {
            debitAccount,
            amount: parseFloat(amount),
            beneficiaryAccount,
            beneficiaryType,
            date // Include date in transfer details
        };

        axios.post('http://localhost:8080/v1/transfers', transferDetails)
            .then(() => {
                Swal.fire({
                    icon: 'success',
                    title: 'Transfer successful',
                    text: `Your transfer of $${amount} to ${beneficiaryAccount} was successful!`,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: 'OK',
                }).then(() => navigate('/'));
            })
            .catch(error => {
                let errorMessage = 'Something went wrong while transferring money.';
                if (error.response && error.response.data && error.response.data.message) {
                    errorMessage = error.response.data.message;
                }

                Swal.fire({
                    icon: 'error',
                    title: 'Transfer failed',
                    text: errorMessage,
                    confirmButtonColor: '#d33',
                    confirmButtonText: 'Try Again',
                });
            });
    };

    return (
        <div className="relative flex items-center justify-center min-h-screen"
             style={{
                 backgroundImage: "url('/mony.jpeg')",
                 backgroundSize: 'cover',
                 backgroundRepeat: 'no-repeat',
                 backgroundPosition: 'center',
                 width: '100%',
                 height: '100%',
             }}>
            <div className="absolute inset-0 bg-black opacity-20"></div>

            <form onSubmit={handleSubmit} className="relative bg-white p-10 rounded-lg shadow-xl w-full max-w-lg">
                <h1 className="text-4xl font-extrabold mb-8 text-center text-blue-600">Transfer Money</h1>

                <div className="mb-6">
                    <label className="block text-lg font-medium text-white mb-2" htmlFor="debitAccount">Debit Account</label>
                    <select
                        id="debitAccount"
                        value={debitAccount}
                        onChange={(e) => setDebitAccount(e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        required
                    >
                        <option value="" disabled>Select a debit account</option>
                        {accounts.map((account) => (
                            <option key={account.id} value={account.accountNumber}>
                                {account.accountNumber} {account.accountName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="mb-6">
                    <label className="block text-lg font-medium text-gray-700 mb-2" htmlFor="amount">Amount</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        placeholder="Enter amount"
                        required
                    />
                </div>

                <div className="mb-6">
                    <label className="block text-lg font-medium text-gray-700 mb-2" htmlFor="beneficiaryType">Beneficiary Type</label>
                    <select
                        id="beneficiaryType"
                        value={beneficiaryType}
                        onChange={(e) => setBeneficiaryType(e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        required
                    >
                        <option value="" disabled>Select beneficiary type</option>
                        <option value="Phone Number">Phone Number</option>
                        <option value="Alias">Alias</option>
                        <option value="IBAN">IBAN</option>
                    </select>
                </div>

                <div className="mb-6">
                    <label className="block text-lg font-medium text-gray-700 mb-2" htmlFor="beneficiaryAccount">Beneficiary Account</label>
                    <input
                        type="text"
                        id="beneficiaryAccount"
                        value={beneficiaryAccount}
                        onChange={(e) => setBeneficiaryAccount(e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        placeholder="Enter beneficiary account number"
                        required
                    />
                </div>

                {/* Date */}
                <div className="mb-6">
                    <label className="block text-lg font-medium text-gray-700 mb-2" htmlFor="date">Transfer Date</label>
                    <input
                        type="date"
                        id="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="w-full bg-gradient-to-r from-blue-500 to-blue-600 text-white py-3 rounded-lg hover:shadow-lg transition duration-300 ease-in-out"
                >
                    Submit Transfer
                </button>
            </form>
        </div>
    );
}

export default TransferMoney;
