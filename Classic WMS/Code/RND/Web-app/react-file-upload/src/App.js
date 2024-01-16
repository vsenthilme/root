import React from "react";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import UploadFiles from "./components/upload-files.component";

function App() {
  return (
    <div className="container text-secondary" >
      <div>
        <h3 className="mt-5">E-Comm Portal [Port:3000]</h3>
		<br/>
        <h4 className="mt-1">Consignment Outbound Orders - Send</h4>
      </div>

      <UploadFiles />
    </div>
  );
}

export default App;