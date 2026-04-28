import { Routes, Route, NavLink } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import Foyers from './pages/Foyers'
import Allocations from './pages/Allocations'
import Rapports from './pages/Rapports'

export default function App() {
  return (
    <>
      <nav>
        <strong>ElectriMada</strong>
        <NavLink to="/" end>Dashboard</NavLink>
        <NavLink to="/foyers">Foyers</NavLink>
        <NavLink to="/allocations">Allocations</NavLink>
        <NavLink to="/rapports">Rapports</NavLink>
      </nav>
      <div className="container">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/foyers" element={<Foyers />} />
          <Route path="/allocations" element={<Allocations />} />
          <Route path="/rapports" element={<Rapports />} />
        </Routes>
      </div>
    </>
  )
}

