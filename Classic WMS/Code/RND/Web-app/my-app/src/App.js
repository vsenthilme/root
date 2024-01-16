import React from "react";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import UploadFiles from "./components/upload-files.component";

function App() {
  return (
    <div className="container text-secondary" >
      <div>
        <h1 className="mt-5">Service Provider Portal</h1>
        <h2 className="mt-5">Received Orders from E-Comm [Port:3000]</h2>
      </div>

      <UploadFiles />
    </div>
  );
}

export default App;