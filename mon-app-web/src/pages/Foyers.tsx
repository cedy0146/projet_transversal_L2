import { useEffect, useState } from 'react'
import axios from 'axios'

interface Foyer {
  idFoyer: string
  nom: string
  localisation: string
  besoinKwh: number
  typePriorite: string
  joursSansElectricite: number
}

export default function Foyers() {
  const [foyers, setFoyers] = useState<Foyer[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    axios.get('/api/foyers')
      .then(r => { setFoyers(r.data.data); setLoading(false) })
      .catch(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading">Chargement...</div>

  return (
    <div>
      <h1>Foyers electrifiees</h1>
      <div className="grid">
        {foyers.map(f => (
          <div className="card" key={f.idFoyer}>
            <h3>{f.nom}</h3>
            <p><strong>Localisation:</strong> {f.localisation}</p>
            <p><strong>Besoin:</strong> {f.besoinKwh} kWh</p>
            <p><strong>Priorite:</strong> <span className={`badge badge-${f.typePriorite?.toLowerCase() || 'normal'}`}>{f.typePriorite}</span></p>
            <p><strong>Jours sans electricite:</strong> {f.joursSansElectricite}</p>
          </div>
        ))}
      </div>
    </div>
  )
}

