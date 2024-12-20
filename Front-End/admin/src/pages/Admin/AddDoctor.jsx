import React, { useEffect, useState } from 'react'
import { assets } from '../../assets/assets'
import { toast } from 'react-toastify'
import { useDispatch, useSelector } from 'react-redux'
import { getAllSpecialties, refreshError, refreshMessage } from '../../state/speciality/Actions'
import { addDoctor } from '../../state/doctor/Actions'

const AddDoctor = () => {
  const successMessage = useSelector((state) => state.doctor.message);
  const errorMessage = useSelector((state) => state.doctor.error);
  const specialities = useSelector((state) => state.speciality.specialities);

  const dispatch = useDispatch();

  const [docImg, setDocImg] = useState(null);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [experience, setExperience] = useState('1');
  const [fee, setFee] = useState('');
  const [about, setAbout] = useState('');
  const [speciality, setSpeciality] = useState(specialities[0].id);
  const [degree, setDegree] = useState('');
  const [address, setAddress] = useState('');
  const [errors, setErrors] = useState({
    name: '',
    email: '',
    password: '',
    fees: '',
    image: '',
    education: '',
    address: ''
  });

  useEffect(() => {
    dispatch(getAllSpecialties());
  }, []);

  // Track the success and error messages
  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage || 'Doctor added successfully!');
      dispatch(refreshMessage());
      setDocImg(null);
      setName('');
      setEmail('');
      setPassword('');
      setFee('');
      setDegree('');
      setAddress('');
      setAbout('');
      setErrors({ name: '', email: '', password: '', fees: '', image: '', education: '', address: '' });
    }

    if (errorMessage) {
      toast.error(errorMessage);
      dispatch(refreshError());
    }
  }, [successMessage, errorMessage, dispatch]);

  // Validate form before submitting
  const validateForm = () => {
    let formErrors = { name: '', email: '', password: '', fees: '', image: '', education: '', address: '' };
    let isValid = true;

    // Validate doctor name (No special characters, length between 3 and 50)
    const nameRegex = /^[a-zA-Z0-9\s]+$/;
    if (!name.trim()) {
      formErrors.name = 'Name is required';
      isValid = false;
    } else if (name.length < 3 || name.length > 50) {
      formErrors.name = 'Name must be between 3 and 50 characters';
      isValid = false;
    } else if (!nameRegex.test(name)) {
      formErrors.name = 'Name cannot contain special characters';
      isValid = false;
    }

    // Validate email (length <= 50)
    if (!email.trim()) {
      formErrors.email = 'Email is required';
      isValid = false;
    } else if (email.length > 50) {
      formErrors.email = 'Email cannot exceed 50 characters';
      isValid = false;
    }

    // Validate password (length >= 8)
    if (!password.trim()) {
      formErrors.password = 'Password is required';
      isValid = false;
    } else if (password.length < 8) {
      formErrors.password = 'Password must be at least 8 characters';
      isValid = false;
    }

    // Validate fees (must be a positive number)
    if (!fee.trim()) {
      formErrors.fees = 'Fees is required';
      isValid = false;
    } else if (isNaN(fee) || Number(fee) <= 0) {
      formErrors.fees = 'Fees must be a positive number';
      isValid = false;
    }

    // Validate education (length >= 3)
    if (!degree.trim()) {
      formErrors.education = 'Education is required';
      isValid = false;
    } else if (degree.length < 3) {
      formErrors.education = 'Education must be at least 3 characters';
      isValid = false;
    }

    // Validate address (length >= 3)
    if (!address.trim()) {
      formErrors.address = 'Address is required';
      isValid = false;
    } else if (address.length < 3) {
      formErrors.address = 'Address must be at least 3 characters';
      isValid = false;
    }

    // Check if file is selected and is an SVG or other allowed types
    if (!docImg) {
      formErrors.image = 'Image Not Selected';
      isValid = false;
    } else {
      const allowedExtensions = ['image/svg+xml', 'image/png', 'image/jpeg', 'image/jpg'];
      if (!allowedExtensions.includes(docImg.type)) {
        formErrors.image = 'Invalid file type. Only SVG, PNG, or JPEG are allowed.';
        isValid = false;
      }
    }

    setErrors(formErrors); // Update error state
    return isValid;
  };

  const onFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // Validate file type
      const allowedTypes = ['image/jpeg', 'image/png', 'image/svg+xml', 'image/jpg'];
      if (!allowedTypes.includes(file.type)) {
        toast.error('Only JPG, PNG, and SVG files are allowed');
        return;
      }
      setDocImg(file);
    }
  };

  const onSubmitHandler = async (event) => {
    event.preventDefault();

    if (!validateForm()) {
      toast.error('Please fix the errors before submit');
      return;
    }

    try {
      if (!docImg) return toast.error('Image Not Selected');

      // Prepare FormData
      const formData = new FormData();
      const doctorData = {
        name: name,
        email: email,
        password: password,
        experience: experience,
        fees: fee,
        speciality: speciality,
        education: degree,
        address: address,
        about: about
      };
      formData.append('doctor', new Blob([JSON
        .stringify(doctorData)], {
        type: 'application/json'
      }));
      formData.append('image', docImg);

      formData.forEach((value, key) => {
        console.log(`${key}: ${value}`);
      })

      dispatch(addDoctor(formData));
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to add doctor');
    }
  }

  return (
    <form onSubmit={onSubmitHandler} className='m-5 w-full'>
      <p className='mb-3 text-lg font-medium'>Add Doctor</p>

      <div className='bg-white px-8 py-8 border rounded w-full max-w-4xl max-h-[80vh] overflow-y-scroll'>
        <div className='flex items-center gap-4 mb-8 text-gray-500'>
          <label htmlFor="doc-img">
            <img className='w-16 bg-gray-100 rounded-full cursor-pointer' src={docImg ? URL.createObjectURL(docImg) : assets.upload_area} alt="" />
          </label>
          <input onChange={(e) => setDocImg(e.target.files[0])} type="file" id='doc-img' hidden />
          <p>Upload doctor <br /> picture</p>
          {errors.image && <p className="text-red-500 text-sm">{errors.image}</p>}
        </div>

        <div className='flex flex-col lg:flex-row items-start gap-10 text-gray-600'>
          <div className='w-full lg-flex-1 flex flex-col gap-4'>
            <div className='flex-1 flex flex-col gap-1'>
              <p>Doctor name</p>
              <input onChange={(e) => setName(e.target.value)} value={name} className='border rounded px-3 py-2' type="text" placeholder='Name' required />
              {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Doctor Email</p>
              <input onChange={(e) => setEmail(e.target.value)} value={email} className='border rounded px-3 py-2' type="email" placeholder='Doctor email' required />
              {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Doctor password</p>
              <input onChange={(e) => setPassword(e.target.value)} value={password} className='border rounded px-3 py-2' type="password" placeholder='Password' required />
              {errors.password && <p className="text-red-500 text-sm">{errors.password}</p>}
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Experience</p>
              <select onChange={(e) => setExperience(e.target.value)} value={experience} className='border rounded px-3 py-2' name="" id="">
                <option value="1">1 Year</option>
                <option value="2">2 Year</option>
                <option value="3">3 Year</option>
                <option value="4">4 Year</option>
                <option value="5">5 Year</option>
                <option value="6">6 Year</option>
                <option value="7">7 Year</option>
                <option value="8">8 Year</option>
                <option value="9">9 Year</option>
                <option value="10">10 Year</option>
              </select>
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Fees</p>
              <input onChange={(e) => setFee(e.target.value)} value={fee} className='border rounded px-3 py-2' type="number" placeholder='Doctor fees' required />
              {errors.fees && <p className="text-red-500 text-sm">{errors.fees}</p>}
            </div>
          </div>

          <div className='w-full lg:flex-1 flex flex-col gap-4'>
            <div className='flex-1 flex flex-col gap-1'>
              <p>Speciality</p>
              <select onChange={(e) => setSpeciality(e.target.value)} value={speciality} className='border rounded px-3 py-2' name="" id="">
                {specialities.map((item, index) => (
                  <option key={index} value={item.id}>
                    {item.name}
                  </option>
                ))}
              </select>
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Education</p>
              <input onChange={(e) => setDegree(e.target.value)} value={degree} className='border rounded px-3 py-2' type="text" placeholder='education' required />
              {errors.education && <p className="text-red-500 text-sm">{errors.education}</p>}
            </div>

            <div className='flex-1 flex flex-col gap-1'>
              <p>Address</p>
              <input onChange={(e) => setAddress(e.target.value)} value={address} className='border rounded px-3 py-2' type="text" placeholder='Address 1' required />
              {errors.address && <p className="text-red-500 text-sm">{errors.address}</p>}
            </div>
          </div>
        </div>

        <div>
          <p className='mt-4 mb-2'>About Doctor</p>
          <textarea onChange={(e) => setAbout(e.target.value)} value={about} className='w-full px-4 pt-2 border rounded' rows={5} placeholder='Write about doctor' />
        </div>

        <button type='submit' className='bg-primary px-10 py-3 mt-4 text-white rounded-full'>Add Doctor</button>

      </div>
    </form>
  )
}

export default AddDoctor