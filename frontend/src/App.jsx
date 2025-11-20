import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [user, setUser] = useState(null);
  const [photos, setPhotos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('browse'); // browse, upload, profile

  // Check if user is logged in
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      // Verify token and get user info
      fetch('/api/auth/verify', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => response.json())
      .then(data => {
        if (data.user) {
          setUser(data.user);
        }
      })
      .catch(err => {
        console.error('Error verifying token:', err);
        localStorage.removeItem('token');
      });
    }
  }, []);

  const handleLogin = async (email, password) => {
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      });

      const data = await response.json();

      if (data.token) {
        localStorage.setItem('token', data.token);
        setUser(data.user);
        setError('');
      } else {
        setError(data.message || 'Login failed');
      }
    } catch (err) {
      setError('Network error');
    }
  };

  const handleRegister = async (name, email, password) => {
    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, email, password })
      });

      const data = await response.json();

      if (data.token) {
        localStorage.setItem('token', data.token);
        setUser(data.user);
        setError('');
      } else {
        setError(data.message || 'Registration failed');
      }
    } catch (err) {
      setError('Network error');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  const uploadPhoto = async (file) => {
    if (!file) return;

    const formData = new FormData();
    formData.append('photo', file);

    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      
      const response = await fetch('/api/photos/upload', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: formData
      });

      const data = await response.json();

      if (data.success) {
        // Refresh photos
        loadPhotos();
      } else {
        setError(data.message || 'Upload failed');
      }
    } catch (err) {
      setError('Upload error');
    } finally {
      setLoading(false);
    }
  };

  const loadPhotos = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/photos', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      const data = await response.json();
      setPhotos(data.photos || []);
    } catch (err) {
      setError('Error loading photos');
    }
  };

  useEffect(() => {
    if (user) {
      loadPhotos();
    }
  }, [user]);

  if (!user) {
    return (
      <div className="auth-container">
        <AuthForm 
          onLogin={handleLogin} 
          onRegister={handleRegister} 
          error={error} 
        />
      </div>
    );
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>Photo Storage</h1>
        <div className="user-info">
          <span>Welcome, {user.name}!</span>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </header>

      <nav className="tabs">
        <button 
          className={activeTab === 'browse' ? 'active' : ''} 
          onClick={() => setActiveTab('browse')}
        >
          Browse Photos
        </button>
        <button 
          className={activeTab === 'upload' ? 'active' : ''} 
          onClick={() => setActiveTab('upload')}
        >
          Upload Photos
        </button>
        <button 
          className={activeTab === 'profile' ? 'active' : ''} 
          onClick={() => setActiveTab('profile')}
        >
          Profile
        </button>
      </nav>

      <main className="main-content">
        {activeTab === 'browse' && <PhotoGallery photos={photos} />}
        {activeTab === 'upload' && <UploadForm onUpload={uploadPhoto} loading={loading} />}
        {activeTab === 'profile' && <UserProfile user={user} />}
      </main>
    </div>
  );
}

// AuthForm Component
function AuthForm({ onLogin, onRegister, error }) {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (isLogin) {
      onLogin(formData.email, formData.password);
    } else {
      onRegister(formData.name, formData.email, formData.password);
    }
  };

  return (
    <div className="auth-form">
      <h2>{isLogin ? 'Login' : 'Register'}</h2>
      
      {!isLogin && (
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
      )}
      
      <div className="form-group">
        <label htmlFor="email">Email:</label>
        <input
          type="email"
          id="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
        />
      </div>
      
      <div className="form-group">
        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          required
        />
      </div>
      
      {error && <div className="error">{error}</div>}
      
      <button type="submit" onClick={handleSubmit}>
        {isLogin ? 'Login' : 'Register'}
      </button>
      
      <p>
        {isLogin ? "Don't have an account?" : "Already have an account?"}{' '}
        <button type="button" onClick={() => setIsLogin(!isLogin)} className="switch-mode">
          {isLogin ? 'Register' : 'Login'}
        </button>
      </p>
    </div>
  );
}

// PhotoGallery Component
function PhotoGallery({ photos }) {
  return (
    <div className="photo-gallery">
      <h2>Your Photos</h2>
      {photos.length === 0 ? (
        <p>No photos uploaded yet.</p>
      ) : (
        <div className="photos-grid">
          {photos.map(photo => (
            <div key={photo.id} className="photo-card">
              <img 
                src={photo.url} 
                alt={photo.filename} 
                onError={(e) => {
                  e.target.src = '/placeholder.jpg'; // fallback image
                }}
              />
              <div className="photo-info">
                <p>File: {photo.filename}</p>
                <p>Size: {(photo.size / 1024).toFixed(2)} KB</p>
                <p>Uploaded: {new Date(photo.created_at).toLocaleString()}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// UploadForm Component
function UploadForm({ onUpload, loading }) {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSelectedFile(file);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedFile) {
      onUpload(selectedFile);
      setSelectedFile(null);
    }
  };

  return (
    <div className="upload-form">
      <h2>Upload Photo</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="photo">Choose Photo:</label>
          <input
            type="file"
            id="photo"
            name="photo"
            accept="image/*"
            onChange={handleFileChange}
            required
          />
        </div>
        
        {selectedFile && (
          <div className="file-info">
            <p>Selected file: {selectedFile.name}</p>
            <p>Size: {(selectedFile.size / 1024).toFixed(2)} KB</p>
          </div>
        )}
        
        <button type="submit" disabled={loading || !selectedFile}>
          {loading ? 'Uploading...' : 'Upload'}
        </button>
      </form>
    </div>
  );
}

// UserProfile Component
function UserProfile({ user }) {
  return (
    <div className="user-profile">
      <h2>Profile</h2>
      <div className="profile-info">
        <p><strong>Name:</strong> {user.name}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Role:</strong> {user.role}</p>
        <p><strong>Storage Used:</strong> {(user.storage_used / 1024 / 1024).toFixed(2)} MB</p>
        <p><strong>Storage Limit:</strong> {(user.storage_limit / 1024 / 1024).toFixed(2)} MB</p>
      </div>
    </div>
  );
}

export default App;