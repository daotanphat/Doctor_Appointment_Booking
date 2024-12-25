import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { getUserInfo, refreshError, refreshMessage, updateUserInfo } from '../../state/user/Actions';
import { assets } from '../assets/assets'
import avatarDefault from '../assets/avatar_default.jpg';
import { toast } from 'react-toastify';

const MyProfile = () => {

  const user = useSelector((state) => state.user.user);
  const successMessage = useSelector((state) => state.user.message);
  const errorMessage = useSelector((state) => state.user.error);
  const token = localStorage.getItem('token');

  const dispatch = useDispatch();

  const [userData, setUserData] = useState(null);
  const [isEdit, setIsEdit] = useState(false);
  const [userImg, setUserImg] = useState(null);
  const [email, setEmail] = useState(user?.email);
  const [name, setName] = useState(user?.fullName);
  const [dob, setDob] = useState(user?.dateOfBirth == null ? '' : user.dateOfBirth);
  const [address, setAddress] = useState(user?.address == null ? '' : user.address);
  const [phone, setPhone] = useState(user?.phone == null ? '' : user.address);
  const [gender, setGender] = useState(user?.gender == null ? true : user.gender);
  const [errors, setErrors] = useState({
    name: '',
    dob: '',
    address: '',
    phone: '',
    gender: '',
    image: ''
  });

  useEffect(() => {
    dispatch(getUserInfo());
  }, [dispatch]);

  // Update userData whenever the Redux user state changes
  useEffect(() => {
    if (user) {
      setUserData(user);
    }
  }, [user]);

  // Track the success and error messages
  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage || 'Update info successfully!');
      dispatch(refreshMessage());
      setErrors({ name: '', dob: '', address: '', phone: '', gender: '', image: '' });
    }

    if (errorMessage) {
      toast.error(errorMessage);
      dispatch(refreshError());
    }
  }, [successMessage, errorMessage, dispatch]);

  // Validate form before submitting
  const validateForm = () => {
    let formErrors = { name: '', dob: '', address: '', phone: '', gender: '', image: '' };
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

    // Validate dob (Must be a valid date in the past)
    const today = new Date();
    const dobDate = new Date(dob);
    if (!dob) {
      formErrors.dob = 'Date of Birth is required';
      isValid = false;
    } else if (isNaN(dobDate.getTime())) {
      formErrors.dob = 'Invalid date format';
      isValid = false;
    } else if (dobDate >= today) {
      formErrors.dob = 'Date of Birth must be in the past';
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

    // Validate phone (Must be exactly 10 digits)
    const phoneRegex = /^\d{10}$/;
    if (!phone.trim()) {
      formErrors.phone = 'Phone number is required';
      isValid = false;
    } else if (!phoneRegex.test(phone)) {
      formErrors.phone = 'Phone number must be exactly 10 digits';
      isValid = false;
    }

    // Check if file is selected and is an SVG or other allowed types
    if (userImg) {
      const allowedExtensions = ['image/svg+xml', 'image/png', 'image/jpeg', 'image/jpg'];
      if (!allowedExtensions.includes(userImg.type)) {
        formErrors.image = 'Invalid file type. Only SVG, PNG, or JPEG are allowed.';
        isValid = false;
      }
    }

    setErrors(formErrors); // Update error state
    return isValid;
  };

  const onSubmitHandler = async (event) => {
    event.preventDefault();

    if (!validateForm()) {
      toast.error('Please fix the errors before submit');
      return;
    }

    try {
      // Prepare FormData
      const formData = new FormData();
      const userData = {
        email: email,
        fullName: name,
        dateOfBirth: dob,
        phone: phone,
        address: address,
        gender: gender
      };
      formData.append('user', new Blob([JSON
        .stringify(userData)], {
        type: 'application/json'
      }));
      formData.append('image', userImg);

      formData.forEach((value, key) => {
        console.log(`${key}: ${value}`);
      })
      console.log(userData);

      dispatch(updateUserInfo(formData, token));
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to update user info');
      console.log(error);
    }
  }

  return token && userData && (
    <form onSubmit={onSubmitHandler} className='max-w-lg flex flex-col gap-2 text-sm'>
      {/* <img className='w-36 rounded' src={userData?.imageUrl || avatarDefault} alt="" /> */}

      <div className='flex items-center gap-4 mb-8 text-gray-500'>
        <label htmlFor="user-img">
          <img className='w-36 rounded cursor-pointer' src={userImg ? URL.createObjectURL(userImg) : assets.upload_area} alt="" />
        </label>
        <input onChange={(e) => setUserImg(e.target.files[0])} type="file" id='user-img' hidden />
        <p>Upload your<br /> picture</p>
        {errors.image && <p className="text-red-500 text-sm">{errors.image}</p>}
      </div>

      {
        isEdit
          ?
          <input className='bg-gray-50 text-3xl font-medium max-w-60 mt-4' type="text" value={name || userData?.fullName} onChange={(e) => setName(e.target.value)} />
          : <p className='font-medium text-3xl text-neutral-800 mt-4'>{userData?.fullName}</p>
      }{errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}

      <hr className='bg-zinc-400 h-[1px] border-none' />
      <div>
        <p className='text-neutral-500 underline mt-3'>CONTACT INFORMATION</p>
        <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700'>
          <p className='font-medium'>Email id:</p>
          <p className='text-blue-500'>{userData?.email}</p>
          <p className='font-medium'>Phone</p>
          <div>
            {
              isEdit
                ? <div>
                  <input className='bg-gray-100 max-w-52' type="text" value={phone || userData?.phone} onChange={(e) => setPhone(e.target.value)} />
                </div>
                : <p className='text-blue-400'>{userData?.phone}</p>
            }{errors.phone && <p className="text-red-500 text-sm">{errors.phone}</p>}
          </div>
          <p className='font-medium'>Address</p>
          <div>
            {
              isEdit
                ? <p>
                  <input className='bg-gray-50' onChange={(e) => setAddress(e.target.value)} value={address || userData?.address} type="text" />
                </p>
                : <p className='text-gray-500'>
                  {userData?.address}
                </p>
            }{errors.address && <p className="text-red-500 text-sm">{errors.address}</p>}
          </div>
        </div>
      </div>
      <div>
        <p className='text-neutral-500 underline mt-3'>BASIC INFORMATION</p>
        <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700'>
          <p className='font-medium'>Gender:</p>
          <div>
            {
              isEdit
                ? <select className='max-w-20 bg-gray-100' onChange={(e) => setGender(e.target.value)} value={gender || userData?.gender}>
                  <option value='true'>Male</option>
                  <option value='false'>Female</option>
                </select>
                : <p className='text-gray-400'>{userData?.gender ? 'Male' : 'Female'}</p>
            }{errors.gender && <p className="text-red-500 text-sm">{errors.gender}</p>}
          </div>
          <p className='font-medium'>Birthday</p>
          <div>
            {
              isEdit
                ? <div>
                  <input className='max-w-28 bg-gray-100' type="date" value={dob || userData?.dateOfBirth} onChange={(e) => setDob(e.target.value)} />
                </div>
                : <p className='text-gray-400'>{userData?.dateOfBirth}</p>
            }{errors.dob && <p className="text-red-500 text-sm">{errors.dob}</p>}
          </div>
        </div>
      </div>

      <div className='mt-10'>
        {
          isEdit
            ? <button className='border border-primary px-8 py-2 rounded-full hover:bg-primary hover:text-white transition-all' type="submit" onClick={() => setIsEdit(false)}>Save information</button>
            : <button className='border border-primary px-8 py-2 rounded-full hover:bg-primary hover:text-white transition-all' onClick={(e) => { e.preventDefault(); setIsEdit(true); }}>Edit</button>
        }
      </div>
    </form>
  )
}

export default MyProfile