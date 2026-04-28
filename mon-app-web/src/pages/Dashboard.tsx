import { useEffect, useState } from 'react'
import axios from 'axios'

interface DashboardData {
  batterie: any
  prevision: any
  alertes: any[]
  stats: { foyersTotal: number; demandesAujourdhui: number; tauxSatisfaction: number }
}

export default function Dashboard() {
  const [data, setData] = useState<DashboardData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    axios.get('/api/dashboard')
      .then(r => { setData(r.data.data); setLoading(false) })
      .catch(e => { setError(e.message); setLoading(false) })
  }, [])

  if (loading) return <div className="loading">Chargement...</div>
  if (error) return <div className="alert alert-danger">Erreur: {error}</div>
  if (!data) return null

  return (
    <div>
      <h1>Dashboard</h1>
      <div className="grid">
        <div className="card">
          <h2>Statistiques</h2>
          <p><strong>Foyers:</strong> {data.stats.foyersTotal}</p>
          <p><strong>Demandes aujourd'hui:</strong> {data.stats.demandesAujourdhui}</p>
          <p><strong>Taux satisfaction:</strong> {data.stats.tauxSatisfaction}%</p>
        </div>
        <div className="card">
          <h2>Batterie</h2>
          {data.batterie ? (
            <>
              <p><strong>Capacité:</strong> {data.batterie.capaciteActuelle} / {data.batterie.capaciteTotale} kWh</p>
              <p><strong>Seuil critique:</strong> {data.batterie.seuilCritique}%</p>
            </>
          ) : <p>Aucune batterie</p>}
        </div>
        <div className="card">
          <h2>Prévision</h2>
          {data.prevision ? (
            <>
              <p><strong>Production estimée:</strong> {data.prevision.productionEstimee} kWh</p>
              <p><strong>Confiance:</strong> {data.prevision.confiance}%</p>
            </>
          ) : <p>Aucune prévision</p>}
        </div>
        <div className="card">
          <h2>Alertes ({data.alertes.length})</h2>
          {data.alertes.length === 0 ? <p>Aucune alerte</p> :
            data.alertes.slice(0, 5).map((a: any) => (
              <div key={a.idAlerte} className="alert alert-danger" style={{marginBottom: '0.5rem'}}>
                {a.message} ({a.niveau})
              </div>
            ))}
        </div>
      </div>
    </div>
  )
}

