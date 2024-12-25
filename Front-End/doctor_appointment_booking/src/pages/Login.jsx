import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { login, registerUser } from '../../state/Authentication/Actions';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const errorMessage = useSelector((state) => state.auth.error);
  const successMessage = useSelector((state) => state.auth.message);
  const roles = useSelector((state) => state.auth.roles);

  const [state, setState] = useState('Sign Up');

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');

  // Error messages state
  const [errors, setErrors] = useState({
    fullName: '',
    email: '',
    password: '',
  });

  // Monitor errorMessage, successMessage, and roles changes
  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }

    if (successMessage) {
      toast.success(successMessage);
      // Clear the input fields
      setName('');
      setEmail('');
      setPassword('');
    }

    // If user has a valid role, navigate to the appropriate page
    if (roles && roles.includes('USER')) {
      navigate('/');
    }
  }, [errorMessage, successMessage, roles, navigate]);

  const validateForm = () => {
    const nameRegex = /^[a-zA-Z\s]+$/; // Only alphabets and spaces
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Email format
    let errors = {};

    if (state === 'Sign Up') {
      // Full Name validation
      if (!name || name.length < 3 || name.length > 50 || !nameRegex.test(name)) {
        errors.fullName = 'Full Name must be 3-50 characters long and not contain special characters.';
      }

      // Email validation
      if (!email || email.length > 30 || !emailRegex.test(email)) {
        errors.email = 'Enter a valid email address with at most 30 characters.';
      }

      // Password validation
      if (!password || password.length < 6 || password.length > 30) {
        errors.password = 'Password must be 6-30 characters long.';
      }
    }

    setErrors(errors);
    return Object.keys(errors).length === 0; // Return true if no errors
  };

  const onSubmitHandler = async (event) => {
    event.preventDefault();

    if (state === 'Sign up') {
      if (validateForm()) {
        const formData = {
          fullName: name,
          email: email,
          password: password,
        };
        setErrors({}); // Clear errors after successful submission

        try {
          await dispatch(registerUser({ requestData: formData, navigate: navigate }));
          if (successMessage) {
            toast.success(successMessage || 'Registered successfully!');
            setState('Login');
          }
        } catch (error) {
          // Assuming the error is set in the Redux state
          setErrors({ email: error.message }); // Set the error message for email
        }
      }
    } else {
      const formData = {
        email: email,
        password: password,
      };

      try {
        await dispatch(login(formData));
      } catch (error) {
        // Assuming the error is set in the Redux state
        console.log(error);
      }
    }
  }

  return (
    <form className='min-h-[80vh] flex items-center'>
      <div className='flex flex-col gap-3 m-auto items-start p-8 min-w-[340px] sm:min-w-96 border rounded-xl text-zinc-600 text-sm shadow-lg'>
        <p className='text-2xl font-semibold'>{state === 'Sign Up' ? 'Create Account' : 'Login'}</p>
        {errorMessage && ( // Display the error message from the backend
          <p className="text-red-500 text-xs mt-1">{errorMessage}</p>
        )}
        <p>Please {state === 'Sign Up' ? 'sign up' : 'login'} to book appointment</p>
        {
          state === 'Sign Up' &&
          <div className='w-full'>
            <p>Full Name</p>
            <input className='border border-zinc-300 rounded w-full p-2 mt-1' type="text" onChange={(e) => setName(e.target.value)} value={name} required />
            {errors.fullName && (
              <p className="text-red-500 text-xs mt-1">{errors.fullName}</p>
            )}
          </div>
        }
        <div className='w-full'>
          <p>Email</p>
          <input className='border border-zinc-300 rounded w-full p-2 mt-1' type="email" onChange={(e) => setEmail(e.target.value)} value={email} required />
          {errors.email && (
            <p className="text-red-500 text-xs mt-1">{errors.email}</p>
          )}
        </div>
        <div className='w-full'>
          <p>Password</p>
          <input className='border border-zinc-300 rounded w-full p-2 mt-1' type="password" onChange={(e) => setPassword(e.target.value)} value={password} required />
          {errors.password && (
            <p className="text-red-500 text-xs mt-1">{errors.password}</p>
          )}
        </div>
        <button onClick={onSubmitHandler} className='bg-primary text-white w-full py-2 rounded-md text-base'>{state === 'Sign Up' ? 'Create Account' : 'Login'}</button>
        {
          state === 'Sign Up'
            ? <p>Already have an account? <span onClick={() => setState('Login')} className='text-primary underline cursor-pointer'>Login here</span> </p>
            : <p>Create an new account? <span onClick={() => setState('Sign Up')} className='text-primary underline cursor-pointer'>Click here</span> </p>
        }
      </div>
    </form>
  )
}

export default Login