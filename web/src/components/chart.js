import React from 'react';
import C3Chart from 'react-c3js';
import '../static/c3.css';


export default class ChartComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        let negative = this.props.chart.map((entry) => { return entry.negative });
        let total = this.props.chart.map((entry) => { return entry.total });
        negative.unshift("negative");
        total.unshift("total");
        let data = { columns: [ total, negative ] };

        return(
            <C3Chart data={data} style={{ margin: "20px" }} />
        )
    }
}