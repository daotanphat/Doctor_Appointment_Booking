import React, { useContext, useEffect, useState } from 'react'
import { AppContext } from '../context/AppContext'
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { getDoctorBySpeciality } from '../../state/doctor/Actions';

const RelatedDoctors = ({ docId, speciality }) => {
    const doctors = useSelector((state) => state.doctor.doctors);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [relDocs, setRelDocs] = useState([]);

    useEffect(() => {
        dispatch(getDoctorBySpeciality(speciality));
    }, [speciality, docId])

    // Effect to update relDocs when doctors list from store changes
    useEffect(() => {
        if (doctors.length > 0) {
            // Filter doctors to exclude the current one
            const doctorsData = doctors.filter((doc) => doc.doctorId !== docId);
            setRelDocs(doctorsData);
        }
    }, [doctors, docId]);

    return (
        <div className='flex flex-col items-center gap-4 my-16 text-gray-900 md:mx-10'>
            <h1 className='text-3xl font-medium'>Related Doctors</h1>
            <p className='sm:w-1/3 text-center text-sm'>Simply browse through our extensive list of trusted doctors.</p>
            <div className='w-full grid grid-cols-auto gap-4 pt-5 gap-y-6 px-3 sm:px-0'>
                {relDocs.slice(0, 5).map((item, index) => (
                    <div onClick={() => { navigate(`/appointment/${item.doctorId}`); scrollTo(0, 0) }} className='border border-blue rounded-xl overflow-hidden cursor-pointer hover:translate-y-[-10px] transition-all duration-500' key={index}>
                        <img className='bg-blue-50' src={item.imageUrl} />
                        <div className='p-4'>
                            <div className='flex items-center gap-2 text-small text-center text-green-500'>
                                <p className='w-2 h-2 bg-green-500 rounded-full'></p><p>Available</p>
                            </div>
                            <p className='text-gray-900 text-lg font-medium'>{item.name}</p>
                            <p className='text-gray-600 text-sm'>{item.speciality}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default RelatedDoctors