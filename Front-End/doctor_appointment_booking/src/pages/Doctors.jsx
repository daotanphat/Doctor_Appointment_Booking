import React, { useContext, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { AppContext } from '../context/AppContext';
import { useDispatch, useSelector } from 'react-redux';
import { getAllSpecialties } from '../../state/speciality/Actions';
import { getDoctors } from '../../state/doctor/Actions';

const Doctors = () => {
  const specialities = useSelector((state) => state.speciality.specialities);
  const doctors = useSelector((state) => state.doctor.doctors);
  const totalPages = useSelector((state) => state.doctor.totalPages);

  const { speciality: selectedSpeciality } = useParams();
  const [speciality, setSpeciality] = useState(selectedSpeciality || '');
  const [showFilter, setShowFilter] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [page, setPage] = useState(0);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getAllSpecialties());
  }, []);

  useEffect(() => {
    fetchDoctors(); // Fetch doctors whenever `speciality`, `searchTerm`, or `page` changes
  }, [speciality, page]);

  const fetchDoctors = () => {
    dispatch(getDoctors({ search: searchTerm, speciality, page }));
  };

  const handleSpecialityClick = (specialityName) => {
    if (speciality === specialityName) {
      // If the same speciality is clicked again, remove the filter
      setSpeciality('');
    } else {
      // Otherwise, set the clicked speciality as the filter
      setSpeciality(specialityName);
    }
    setPage(0); // Reset to the first page when the filter changes
  };


  const handleSearchClick = () => {
    setPage(0); // Reset to first page when search term changes
    fetchDoctors();
  };

  const handlePrevious = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  const handleNext = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  // Modify the individual page button click handler
  const handlePageClick = (index) => {
    setPage(index);
  };

  return (
    <div>
      <p className='text-gray-600'>Browse through the doctors specialist.</p>

      {/* Search Field */}
      <div className='flex justify-end items-center mt-4'>
        <input
          type='text'
          placeholder='Search by name, speciality, or phone'
          className='border border-gray-300 rounded-l py-2 px-3 w-full sm:w-[300px]'
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button
          onClick={handleSearchClick}
          className='bg-primary text-white px-4 py-2 rounded-r hover:bg-primary-dark transition-all'
        >
          Search
        </button>
      </div>

      <div className='flex flex-col sm:flex-row items-start gap-5 mt-5'>
        <button className={`py-1 px-3 border rounded text-sm transition-all sm:hidden ${showFilter ? 'bg-primary text-white' : ''}`} onClick={() => setShowFilter(prev => !prev)}>Filters</button>
        <div className={`flex-col gap-4 text-sm text-gray-600 ${showFilter ? 'flex' : 'hidden sm:flex'}`}>
          {
            specialities.map((item, index) => (
              <p key={index}
                onClick={() => handleSpecialityClick(item.name)}
                className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === item.name ? 'bg-indigo-100 text-black' : ''}`}>
                {item.name}
              </p>
            ))
          }
        </div>
        <div className='w-full grid grid-cols-auto gap-4 gap-y-6'>
          {
            doctors.map((item, index) => (
              <div onClick={() => navigate(`/appointment/${item.doctorId}`)} className='border border-blue rounded-xl overflow-hidden cursor-pointer hover:translate-y-[-10px] transition-all duration-500' key={index}>
                <img className='bg-blue-50' src={item.imageUrl} />
                <div className='p-4'>
                  <div className='flex items-center gap-2 text-small text-center text-green-500'>
                    <p className='w-2 h-2 bg-green-500 rounded-full'></p><p>Available</p>
                  </div>
                  <p className='text-gray-900 text-lg font-medium'>{item.name}</p>
                  <p className='text-gray-600 text-sm'>{item.speciality}</p>
                </div>
              </div>
            ))
          }
        </div>
      </div>

      {/* Pagination Buttons */}
      <div className="flex justify-end mt-6">
        <button
          disabled={page === 1}
          onClick={handlePrevious}
          className="py-3 px-5 border rounded-l-lg bg-gray-200 hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed text-lg"
        >
          Prev
        </button>
        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={index}
            onClick={() => handlePageClick(index)}
            className={`py-3 px-5 border ${index === page
              ? 'bg-primary text-white'
              : 'bg-gray-200 hover:bg-gray-300'
              } text-lg`}
          >
            {index + 1}
          </button>
        ))}
        <button
          disabled={page === totalPages - 1}
          onClick={handleNext}
          className="py-3 px-5 border rounded-r-lg bg-gray-200 hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed text-lg"
        >
          Next
        </button>
      </div>

    </div>
  )
}

export default Doctors