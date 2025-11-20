# Photo Storage Frontend

This is the React frontend for the photo storage application. It provides a user-friendly interface for uploading, browsing, and managing photos.

## Features

- User authentication (login/register)
- Photo upload functionality
- Photo browsing and management
- User profile with storage information
- Responsive design for all devices

## Prerequisites

- Node.js (v14 or higher)
- Backend server running on port 8000

## Installation

1. Make sure you have the backend server running first
2. Install dependencies:
```bash
npm install
```

## Running the Development Server

To run the development server:

```bash
npm run dev
```

The frontend will be available at `http://localhost:3000` and will proxy API requests to the backend at `http://localhost:8000`.

## Building for Production

To create a production build:

```bash
npm run build
```

This will create a `dist` directory with the bundled application.

## Project Structure

```
frontend/
├── public/               # Static assets
├── src/                  # Source files
│   ├── App.jsx           # Main application component
│   ├── App.css           # Application styles
│   └── main.jsx          # Entry point
├── index.html            # HTML template
├── server.js             # Development server with proxy
├── build.js              # Build script
└── package.json          # Dependencies and scripts
```

## API Integration

The frontend communicates with the backend through the following endpoints:

- `/api/auth/login` - User login
- `/api/auth/register` - User registration
- `/api/auth/verify` - Token verification
- `/api/photos` - Get user photos
- `/api/photos/upload` - Upload photos
- `/api/users/profile` - Get user profile

All API requests are proxied through the development server to avoid CORS issues.