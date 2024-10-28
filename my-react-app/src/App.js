import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TransferMoney from './pages/TransferMoney';
import AccountsList from './pages/AccountsList'; 
import ListTransfer from './pages/ListTransfer';
import UploadCSV from './pages/UploadCSV';

function App() {
  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <div className="relative flex flex-col items-center justify-center min-h-screen bg-cover bg-center bg-no-repeat"
              style={{
                // backgroundImage: "url('/R.jpeg')", 
                backgroundImage: "url('/homeBank.jpg')", 
                
              }}>
              {/* Dark overlay for better contrast */}
              <div className="absolute inset-0 bg-black opacity-20"></div>
              
              {/* Content on top of the background */}
              <div className="relative z-10 text-center">
                {/* Page Title */}
                <h1 className="text-5xl font-extrabold text-white mb-12 tracking-wider drop-shadow-lg">
                  Welcome to Your Banking Portal
                </h1>

                {/* Button Container */}
                <div className="flex flex-col items-center space-y-6">
                  {/* CliQ Button */}
                  <Link to="/transfer">
                    <div className="bg-gradient-to-br from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white px-10 py-4 rounded-full shadow-lg hover:shadow-xl transform transition duration-300 hover:scale-105">
                      <button className="text-lg font-bold focus:outline-none focus:ring-4 focus:ring-blue-300">
                        CliQ
                      </button>
                    </div>
                  </Link>

                  {/* Show Accounts Button */}
                  <Link to="/accounts">
                    <div className="bg-gradient-to-br from-green-400 to-green-600 hover:from-green-500 hover:to-green-700 text-white px-10 py-4 rounded-full shadow-lg hover:shadow-xl transform transition duration-300 hover:scale-105">
                      <button className="text-lg font-bold focus:outline-none focus:ring-4 focus:ring-green-300">
                        Show Accounts
                      </button>
                    </div>
                  </Link>

                  {/* Show Transfers Button */}
                  <Link to="/transferslist">
                    <div className="bg-gradient-to-br from-purple-400 to-purple-600 hover:from-purple-500 hover:to-purple-700 text-white px-10 py-4 rounded-full shadow-lg hover:shadow-xl transform transition duration-300 hover:scale-105">
                      <button className="text-lg font-bold focus:outline-none focus:ring-4 focus:ring-purple-300">
                        Show All Transfers
                      </button>
                    </div>
                  </Link>

                  <Link to="/upload-csv">
                    <div className="bg-gradient-to-br from-purple-400 to-purple-600 hover:from-purple-500 hover:to-purple-700 text-white px-10 py-4 rounded-full shadow-lg hover:shadow-xl transform transition duration-300 hover:scale-105">
                      <button className="text-lg font-bold focus:outline-none focus:ring-4 focus:ring-purple-300">
                        Upload CSV Transfers
                      </button>
                    </div>
                  </Link>
                </div>
              </div>
            </div>
          }
        />
        
        {/* Route for Transfer Money */}
        <Route path="/transfer" element={<TransferMoney />} />
        
        {/* Route for Accounts List */}
        <Route path="/accounts" element={<AccountsList />} />
        
        {/* Route for Transfers List */}
        <Route path="/transferslist" element={<ListTransfer />} />
        <Route path="/upload-csv" element={<UploadCSV />} />

      </Routes>
    </Router>
  );
}

export default App;
