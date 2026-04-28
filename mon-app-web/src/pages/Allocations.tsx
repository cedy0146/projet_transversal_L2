import { useState } from 'react'
import axios from 'axios'

export default function Allocations() {
  const [data, setData] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  const calculer = (mode: string) => {
    setLoading(true)
    axios.get(`/api/allocations?mode=${mode}`)
      .then(r => { setData(r.data.data); setLoading(false) })
      .catch(() => setLoading(false))
  }

  return (
    <div>
      <h1>Allocations d'Energie</h1>
      <div style={{ marginBottom: '1rem', display: 'flex', gap: '0.5rem' }}>
        <button className="btn" onClick={() => calculer('optimise')}>Calculer Optimise</button>
        <button className="btn btn-danger" onClick={() => calculer('baseline')}>Calculer Baseline</button>
      </div>

      {loading && <div className="loading">Calcul en cours...</div>}

      {data && (
        <div className="card">
          <h2>Resultat ({data.mode}) — {data.tempsMs?.toFixed(2)} ms</h2>
          <div className="grid">
            {data.allocations?.map((a: any, i: number) => (
              <div key={i} className="card">
                <p><strong>Foyer:</strong> {a.foyer?.nom || 'N/A'}</p>
                <p><strong>Demande:</strong> {a.demande?.quantiteKwh} kWh</p>
                <p><strong>Criticite:</strong> <span className={`badge badge-${a.demande?.niveauCriticite?.toLowerCase()}`}>{a.demande?.niveauCriticite}</span></p>
                <p><strong>Score Priorite:</strong> {a.scorePriorite?.toFixed(1)}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

