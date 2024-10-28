import React, { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

function UploadCSV() {
    const [file, setFile] = useState(null);
    const [uploadResult, setUploadResult] = useState(null);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!file) {
            Swal.fire({
                icon: 'error',
                title: 'No File Selected',
                text: 'Please select a CSV file to upload.',
                confirmButtonColor: '#d33',
                confirmButtonText: 'OK',
            });
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        axios.post('http://localhost:8080/v1/transfers/upload-csv', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        })
            .then(response => {
                setUploadResult(response.data);
                Swal.fire({
                    icon: 'success',
                    title: 'Upload Successful',
                    text: 'Your CSV file has been uploaded!',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: 'OK',
                });
            })
            .catch(error => {
                let errorMessage = 'Something went wrong while uploading the CSV file.';
                if (error.response && error.response.data && error.response.data.message) {
                    errorMessage = error.response.data.message;
                }
                Swal.fire({
                    icon: 'error',
                    title: 'Upload Failed',
                    text: errorMessage,
                    confirmButtonColor: '#d33',
                    confirmButtonText: 'Try Again',
                });
            });
    };

    return (
        <div className="relative flex items-center justify-center min-h-screen"
             style={{
                 backgroundImage: "url('/money.jpeg')",
                 backgroundSize: 'cover',
                 backgroundRepeat: 'no-repeat',
                 backgroundPosition: 'center',
                 width: '100%',
                 height: '100%',
             }}>
            {/* Dark Overlay */}
            <div className="absolute inset-0 bg-black opacity-20"></div>

            {/* Form Content */}
            <form onSubmit={handleSubmit} className="relative bg-white p-10 rounded-lg shadow-xl w-full max-w-lg">
                <h1 className="text-4xl font-extrabold mb-8 text-center text-blue-600">Upload CSV</h1>

                {/* CSV File Upload */}
                <div className="mb-6">
                    <label className="block text-lg font-medium text-gray-700 mb-2" htmlFor="file">Select CSV File</label>
                    <input
                        type="file"
                        accept=".csv"
                        id="file"
                        onChange={handleFileChange}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:ring focus:ring-blue-200 focus:border-blue-500 transition duration-300 ease-in-out"
                        required
                    />
                </div>

                {/* Submit Button */}
                <button
                    type="submit"
                    className="w-full bg-gradient-to-r from-blue-500 to-blue-600 text-white py-3 rounded-lg hover:shadow-lg transition duration-300 ease-in-out"
                >
                    Upload CSV
                </button>

                {/* Spacer */}
                <div className="mt-8"></div>

                {/* Response Display */}
                {uploadResult && (
                    <div className="mt-6 p-4 border border-gray-300 rounded-lg bg-gray-100">
                        <h2 className="text-lg font-bold text-gray-800">{uploadResult.message}</h2>
                        {uploadResult.totalErrors > 0 && (
                            <div className="mt-2">
                                <p className="text-red-600">Total Errors: {uploadResult.totalErrors}</p>
                                <h3 className="font-semibold">Errors in Transfers:</h3>
                                <div className="overflow-auto">
                                    <pre className="bg-white p-2 border rounded text-gray-700 whitespace-pre-wrap">{JSON.stringify(uploadResult.errorInTransfersResults, null, 2)}</pre>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </form>
        </div>
    );
}

export default UploadCSV;
