const fs = require('fs');
const path = require('path');

// Simple build script to copy files to dist directory
const distDir = path.join(__dirname, 'dist');

// Create dist directory if it doesn't exist
if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Copy index.html to dist
const indexHtml = fs.readFileSync(path.join(__dirname, 'index.html'), 'utf8');
fs.writeFileSync(path.join(distDir, 'index.html'), indexHtml);

// Create a simple bundle by copying the source files
const srcDir = path.join(__dirname, 'src');
const srcDistDir = path.join(distDir, 'src');

if (!fs.existsSync(srcDistDir)) {
  fs.mkdirSync(srcDistDir, { recursive: true });
}

// Copy App.jsx and main.jsx to dist/src
const appJsx = fs.readFileSync(path.join(srcDir, 'App.jsx'), 'utf8');
const mainJsx = fs.readFileSync(path.join(srcDir, 'main.jsx'), 'utf8');
const appCss = fs.readFileSync(path.join(srcDir, 'App.css'), 'utf8');

fs.writeFileSync(path.join(srcDistDir, 'App.jsx'), appJsx);
fs.writeFileSync(path.join(srcDistDir, 'main.jsx'), mainJsx);
fs.writeFileSync(path.join(srcDistDir, 'App.css'), appCss);

// Create a basic HTML file that loads the React app
const bundledHtml = `
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Photo Storage Application</title>
    <style>
      ${appCss}
    </style>
  </head>
  <body>
    <div id="root"></div>
    <script type="importmap">
      {
        "imports": {
          "react": "https://esm.sh/react@19.2.0",
          "react-dom/client": "https://esm.sh/react-dom@19.2.0/client"
        }
      }
    </script>
    <script type="module" src="https://esm.sh/@babel/standalone@7.26.2/babel.min.js"></script>
    <script type="module">
      import React from 'react';
      import { createRoot } from 'react-dom/client';
      import App from './src/App.jsx';
      
      const root = createRoot(document.getElementById('root'));
      root.render(React.createElement(App));
    </script>
  </body>
</html>
`;

fs.writeFileSync(path.join(distDir, 'index.html'), bundledHtml);

console.log('Build completed successfully!');