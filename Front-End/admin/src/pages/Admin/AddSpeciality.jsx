import React, { useEffect, useState } from 'react';
import { assets } from '../../assets/assets';
import { toast } from 'react-toastify';
import { addSpecialty, refreshError, refreshMessage } from '../../state/speciality/Actions';
import { useDispatch, useSelector } from 'react-redux';

const AddSpeciality = () => {
    const successMessage = useSelector((state) => state.speciality.message);
    const errorMessage = useSelector((state) => state.speciality.error);

    const dispatch = useDispatch();

    const [specialityName, setSpecialityName] = useState('');
    const [specialityImage, setSpecialityImage] = useState(null);
    const [errors, setErrors] = useState({
        name: '',
        image: '',
    });

    const [hasSubmitted, setHasSubmitted] = useState(false);

    // Track the success and error messages
    useEffect(() => {
        if (successMessage && hasSubmitted) {
            toast.success(successMessage || 'Speciality added successfully!');
            dispatch(refreshMessage());
            setSpecialityName('');
            setSpecialityImage(null);
            setErrors({ name: '', image: '' });
            setHasSubmitted(false);
        }

        if (errorMessage) {
            toast.error(errorMessage);
            dispatch(refreshError());
        }
    }, [successMessage, errorMessage, hasSubmitted, dispatch]);

    // Validate form before submitting
    const validateForm = () => {
        let formErrors = { name: '', image: '' };
        let isValid = true;

        // Check if name is empty
        if (!specialityName.trim()) {
            formErrors.name = 'Speciality Name cannot be empty';
            isValid = false;
        }

        // Check if name contains special characters
        const regex = /^[a-zA-Z0-9\s]+$/; // Allow only letters, numbers, and spaces
        if (!regex.test(specialityName)) {
            formErrors.name = 'Speciality Name cannot contain special characters';
            isValid = false;
        }

        // Check if file is selected and is an SVG or other allowed types
        if (!specialityImage) {
            formErrors.image = 'Image Not Selected';
            isValid = false;
        } else {
            const allowedExtensions = ['image/svg+xml', 'image/png', 'image/jpeg'];
            if (!allowedExtensions.includes(specialityImage.type)) {
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
            const allowedTypes = ['image/jpeg', 'image/png', 'image/svg+xml'];
            if (!allowedTypes.includes(file.type)) {
                toast.error('Only JPG, PNG, and SVG files are allowed');
                return;
            }
            setSpecialityImage(file);
        }
    };

    const onSubmitHandler = async (event) => {
        event.preventDefault();

        if (!validateForm()) {
            toast.error('Please fix the errors before submit');
            return;
        }

        try {
            if (!specialityImage) return toast.error('Image Not Selected');

            const formData = new FormData();
            formData.append('name', specialityName);
            formData.append('image', specialityImage);

            // Test console log before submitting to server
            formData.forEach((value, key) => console.log(`${key}: ${value}`));

            // Send to API endpoint
            dispatch(addSpecialty(formData));
            setHasSubmitted(true);
            // console.log(errorMessage + 'test');

            // if (errorMessage) {
            //     toast.error(errorMessage);
            //     return;
            // }

            // if (successMessage) {
            //     toast.success(successMessage || 'Speciality added successfully!');
            //     setSpecialityName('');
            //     setSpecialityImage(null);
            //     setErrors({ name: '', image: '' });
            // }
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to add speciality');
        }
    };

    return (
        <form onSubmit={onSubmitHandler} className="m-5 w-full">
            <p className="mb-3 text-lg font-medium">Add Speciality</p>

            <div className="bg-white px-8 py-8 border rounded w-full max-w-4xl">
                <div className="flex flex-col gap-4 text-gray-600">
                    <div className="flex items-center gap-4 mb-4">
                        <label htmlFor="speciality-img">
                            <img
                                className="w-16 bg-gray-100 rounded-full cursor-pointer"
                                src={
                                    specialityImage
                                        ? URL.createObjectURL(specialityImage)
                                        : assets.upload_area
                                }
                                alt="Upload Area"
                            />
                        </label>
                        <input
                            type="file"
                            id="speciality-img"
                            hidden
                            onChange={onFileChange}
                            accept=".jpeg, .jpg, .png, .svg"
                        />
                        <p>Upload Speciality Image</p>
                        {errors.image && <p className="text-red-500 text-sm">{errors.image}</p>}
                    </div>

                    <div className="flex flex-col gap-1">
                        <p>Speciality Name</p>
                        <input
                            type="text"
                            className="border rounded px-3 py-2"
                            placeholder="Speciality Name"
                            value={specialityName}
                            onChange={(e) => setSpecialityName(e.target.value)}
                            required
                        />
                        {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
                    </div>
                </div>

                <button
                    type="submit"
                    className="bg-primary px-10 py-3 mt-4 text-white rounded-full"
                >
                    Add Speciality
                </button>
            </div>
        </form>
    );
};

export default AddSpeciality;